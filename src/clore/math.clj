(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Math helper functions."}
  clore.math)


(defn digits
  "Returns the digits of a number as lazy collection."
  {:added "1.0"}
  [n]
  (letfn [(char-to-digit [c] (- (int c) 48))]
    (map char-to-digit (str n))))
