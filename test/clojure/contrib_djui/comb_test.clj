(ns clojure.contrib-djui.comb-test
  (:use clojure.test)
  (:use clojure.contrib-djui.comb))


(deftest |>-test
  (is (= 42 (|> 7 #(* 3 %) (partial * 2))))
  (is (= 4 (|> 4)))
  (is (= [] (|> [])))
  (is (= nil (|> nil)))
  (is (function? (|> #())))
  (is (thrown? clojure.lang.ArityException (|>))))
