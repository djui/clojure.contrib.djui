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
