(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Collection helper functions."}
  clojure.contrib-djui.coll)


(defn gapmap
  "Returns a lazy sequence consisting of the result of applying f to all
  adjacent values of coll. f should be a function of 2 arguments. If coll
  contains less than 2 items, it is returned and f is not called."
  {:added "1.0"}
  ([f coll]
     (if-let [r (next coll)] ; at least two items?
       (letfn [(gmap [f x coll]
                 (if-let [y (first coll)]
                   (cons (f x y) (gmap f y (next coll)))))]
         (lazy-seq (gmap f (first coll) r)))
       coll)))

(defn unit
  "Returns the value in a collection if it only contains one item."
  {:added "1.0"}
  [coll]
  (when-let [coll (seq coll)]
    (when-not (next coll)
      (first coll))))

(defn any?
  "Returns a boolean if any value in coll satisfies pred."
  {:added "1.0"}
  [pred coll]
  (boolean (some pred coll)))

(defn any-true?
  "Returns a boolean if any value in coll is logically true."
  {:added "1.0"}
  [coll]
  (boolean (some identity coll)))

(defn |>
  "Piping/Thrushing, similar to F#'s or OCaml |>, or Haskells $.
  Limitations: Args are not evaluated lazily and functions can only take one
  argument (as last argument).
  Deprecated with Clojure 1.5, use clojure.core/-> or clojure.core/->> instead."
  {:added "1.0"
   :deprecated "1.1"}
  [& args]
  (reduce #(%2 %) args))
