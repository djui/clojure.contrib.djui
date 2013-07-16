(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Time helper functions."}
  clojure.contrib.djui.time)


(defn bench
  "Returns the execution time, measured in msec, of f. If n is given,
  returns the execution time of running f n-times."
  {:added "1.0"}
  ([f]
     (bench 1 f))
  ([n f]
     (let [start (. System (nanoTime))
           _     (dotimes [_ n] f)
           end   (. System (nanoTime))
           diff  (/ (double (- end start)) 1000000.0)]
       diff)))
