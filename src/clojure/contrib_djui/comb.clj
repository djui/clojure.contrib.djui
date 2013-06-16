(ns ^{:author "Uwe Dauernheim <uwe@dauernheim.net>"
      :doc "Combinator helper functions."}
  clojure.contrib-djui.comb)


(defn |>
  "Piping/Thrushing, similar to F#'s or OCaml |>, or Haskells $."
  {:added "1.0"}
  [& args]
  (reduce #(%2 %) args))
