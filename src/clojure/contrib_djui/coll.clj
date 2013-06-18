(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Collection helper functions."}
  clojure.contrib-djui.coll)


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


;;; Sequentials

(defn unit
  "Returns the value in a collection if it only contains one item."
  {:added "1.0"}
  [coll]
  (when-let [coll (seq coll)]
    (when-not (next coll)
      (first coll))))

(defn any?
  "Returns a boolean representing if any value in coll is true. If pred is
  given, return if any value in coll satisfies pred."
  {:added "1.0"}
  ([coll]
     (any? identity coll))
  ([pred coll]
     (boolean (some pred coll))))

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

(defn |>
  "Piping/Thrushing, similar to F#'s or OCaml |>, or Haskells $.
  Limitations: Args are not evaluated lazily and functions can only take one
  argument (as last argument).
  Deprecated with Clojure 1.5, use clojure.core/-> or clojure.core/->> instead."
  {:added "1.0"
   :deprecated "1.1"}
  [& args]
  (reduce #(%2 %) args))
