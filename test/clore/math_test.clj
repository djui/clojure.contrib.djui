(ns clore.math-test
  (:use clojure.test clore.math))


(deftest digits-test
  (is (= [1] (digits 1)))
  (is (= [1 2] (digits 12)))
  (is (= [] (digits nil))))
