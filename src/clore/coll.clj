(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Collection helper functions."}
  clore.coll)


(defn unit
  "Returns the value in a collection if it only contains one item."
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

(defn- gapmap-internal
  [f x coll]
    (if-let [y (first coll)]
      (cons (f x y) (gapmap-internal f y (next coll)))))

(defn gapmap
  "Returns a lazy sequence consisting of the result of applying f to all
  adjacent values of coll. f should be a function of 2 arguments. If coll
  contains less than 2 items, it is returned and f is not called."
  {:added "1.0"}
  ([f coll]
     (if-let [r (next coll)] ; at least two items?
       (lazy-seq (gapmap-internal f (first coll) r))
       coll)))
