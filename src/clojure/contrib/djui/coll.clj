(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Collection helper functions."}
  clojure.contrib.djui.coll
  (:require [clojure.string :as string]
            [clojure.walk   :as walk]))


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
  {:added "1.5"}
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

;; Don't apply deep-merge with `(fn [x y] y)` as f for better performance.
(defn deep-merge
  "Like merge, but merges maps recursively. If vals are not maps, the last value
  wins."
  {:added "1.9"}
  [& vals]
  (if (every? map? vals)
    (apply merge-with deep-merge vals)
    (last vals)))

;; Courtesy https://github.com/richhickey/clojure-contrib/blob/2ede388a9267d175bfaa7781ee9d57532eb4f20f/src/main/clojure/clojure/contrib/map_utils.clj
(defn deep-merge-with
  "Like merge-with, but merges maps recursively. If vals are not maps,
  (apply f vals) determines the winner."
  {:added "1.9"}
  [f & vals]
  (letfn [(m [& vals]
            (when (some identity vals)
              (if (every? map? vals)
                (apply merge-with m vals)
                (apply f vals))))]
    (apply m vals)))

(defn keywordize
  "Like keyword, but also converting underscore and dots to dashes."
  {:added "1.11"}
  [s]
  (-> (string/lower-case s)
      (string/replace "_" "-")
      (string/replace "." "-")
      (keyword)))

(defn keywordize-keys
  "Like clojure.walk/keywordize-keys but also converting underscore and dots to
  dashes."
  {:added "1.11"}
  [m]
  (let [f (fn [[k v]] (if (string? k) [(keywordize k) v] [k v]))]
    (walk/postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) m)))

(defn- str-keys-to-map
  [[k v]]
  (assoc-in {} (map keyword (string/split k #"[\._]")) v))

(defn deep-keywordize-keys
  "Take a hash map with keys as string and create a nested hash map splitting
  the string by dots and underscores as nesting structure."
  {:added "1.11"}
  [m]
  (->> m (map str-keys-to-map) (apply deep-merge)))


;;; Sequentials

(defn unit
  "Returns the value in a collection if it only contains one item; also know as
  singleton."
  {:added "1.0"}
  [coll]
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
  {:added "1.10"}
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
