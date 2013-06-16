(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Combinator helper functions."}
  clojure.contrib-djui.comb)


(defn |>
  "Piping/Thrushing, similar to F#'s or OCaml |>, or Haskells $.
  Limitations: Args are not evaluated lazily and functions can only take one
  argument (as last argument).
  Deprecated with Clojure 1.5, use clojure.core/-> or clojure.core/->> instead."
  {:added "1.0"
   :deprecated "1.1"}
  [& args]
  (reduce #(%2 %) args))
