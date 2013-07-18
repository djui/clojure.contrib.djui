(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Collection helper functions."}
  clojure.contrib.djui.coll)


;;; Maps

(defn gapmap
  "Returns a lazy sequence consisting of the result of applying f to all
  adjacent values of coll. f should be a function taking 2 arguments. If coll
  contains less than 2 items, it is returned and f is not called."
  {:added "1.0"}
  [f coll]
  (if (< (count coll) 2) coll
      (->> coll (partition 2 1) (map (fn [[x y]] (f x y))))))

(defmacro create-map
  "Return a map with a list of variable's name as keys and the variable's
  values as value."
  {:added "1.4"}
  [& vars]
  (zipmap (map keyword vars) vars))

(defn pmapcat
  "Like mapcat but with parallelized map."
  {:added "1.5"}
  [f & colls]
  (apply concat (apply pmap f colls)))

(defn sort-maps-by
  "Sort a collection of maps on multiple keys. Items with a missing key have
  precedence.

  Example:
  (sort-maps-by [{:a 2 :b 2} {:a 1} {:a 2 :b 1}] :a :b)
    => [{:a 1 :b 2} {:a 2 :b 1} {:a 2 :b 2}]"
  {:added "1.5"}
  [coll & ks]
  (sort-by (apply juxt ks) coll))

(defn distinct-by
  "Like distinct, but using ident as the identity function for elements.

  Example:
  (distinct-by :b [{:a 2, :b 7} {:a 3, :b 7} {:a 4, :b 8}])
    => [{:a 2, :b 7} {:a 4, :b 8}])
  (distinct-by second [[:a 2] [:b 2] [:b 3]])
    => [[:a 2] [:b 3]]
  (= (distinct-by identity [1 1 2 3 4 4 5]) (distinct [1 1 2 3 4 4 5]))
    => true"
  [ident coll]
  (letfn [(lazy-step [xs seen]
            (letfn [(step [[x :as xs] seen]
                      (when-let [s (seq xs)]
                        (let [f (ident x)]
                          (if (contains? seen f)
                            (recur (rest s) seen)
                            (cons x (lazy-step (rest s) (conj seen f)))))))]
              (lazy-seq (step xs seen))))]
    (lazy-step coll #{})))


;;; Sequentials

(defn unit
  "Returns the value in a collection if it only contains one item; also know as
  singleton."
  {:added "1.0"} [coll]
  (when-let [coll (seq coll)]
    (when-not (next coll)
      (first coll))))

(defn to-vector
  "Like vector, but (to-vector nil) returns nil."
  {:added "1.8"}
  [x]
  (if (or (nil? x) (vector? x)) x (vector x)))

(defn to-list
  "Like list, but (to-list nil) returns nil."
  {:added "1.8"}
  [x]
  (if (or (nil? x) (list? x)) x (list x)))

(defn to-sequence
  "Like sequence, but (to-sequence nil) returns nil."
  {:added "1.8"}
  [x]
  (if (or (nil? x) (sequential? x)) x (list x)))

(defn sequential!
  "Ensure x is sequential. If x is nil, nil is returned.
  Deprecated, use to-sequence instead."
  {:added "1.7"
   :deprecated "1.8"}
  [x]
  (to-sequence x))

(defn any?
  "Returns a boolean representing if any value in coll is true. If pred is
  given, return if any value in coll satisfies pred."
  {:added "1.0"}
  ([coll]
     (any? identity coll))
  ([pred coll]
     (boolean (some pred coll))))

(defn tree-seq+
  "Like tree-seq but, optionally, parallelized and with max. traversion limit."
  {:added "1.5"}
  [branch? children root & [{:keys [parallel? limit]
                             :or {parallel? true, limit Double/POSITIVE_INFINITY}}]]
  (let [mp (if parallel? pmapcat mapcat)]
    (letfn [(walk [depth node]
              (when (< depth limit)
                (lazy-seq
                  (cons node
                    (when (branch? node)
                      (mp (partial walk (inc depth)) (children node)))))))]
      (walk 0 root))))

(defn any-true?
  "Returns a boolean if any value in coll is logically true.
  Deprecated: Use (any? coll)."
  {:added "1.0"
   :deprecated "1.4"}
  [coll]
  (boolean (some identity coll)))

(defn alternate
  "Return a list of lists containing the items coll modulo n."
  {:added "1.4"}
  [n coll]
  (let [x (partition n coll)]
    (if (empty? x) x
        (apply map vector x))))

(defn pjuxt
  "Like juxt, but parallelized."
  [& fns]
  (fn [& args] (pmap #(apply % args) fns)))

(defn |>
  "Piping/Thrushing, similar to F#'s or OCaml |>, or Haskells $.
  Limitations: Args are not evaluated lazily and functions can only take one
  argument (as last argument).
  Deprecated with Clojure 1.5, use clojure.core/-> or clojure.core/->> instead."
  {:added "1.0"
   :deprecated "1.1"}
  [& args]
  (reduce #(%2 %) args))
