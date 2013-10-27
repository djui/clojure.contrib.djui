(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Math helper functions."}
  clojure.contrib.djui.math)


(defn digits
  "Returns the digits of a number as lazy collection."
  {:added "1.0"
   :io? false}
  [n]
  (letfn [(char-to-digit [c] (- (int c) 48))]
    (map char-to-digit (str n))))
