(ns clojure.contrib-djui.coll-test
  (:use clojure.test)
  (:use clojure.contrib-djui.coll))


(deftest gapmap-test
  (are [x y] (= x y)
       [1 2 1] (gapmap + [0 1 1 0])
       [2]     (gapmap + [1 1])
       [1]     (gapmap + [1])
       []      (gapmap + [])
       nil     (gapmap + nil)))

(deftest unit-test
  (is (= 1 (unit [1])))
  (is (= nil (unit [1 2])))
  (is (= nil (unit [nil])))
  (is (= nil (unit [])))
  (is (= nil (unit nil))))

(deftest any?-test
  (are [x] (true? x)
       (any? #(= 1 %) [1 true false nil])
       (any? true? [1 true false nil]))
  (are [x] (false? x)
       (any? #(= 2 %) [1 true false nil])
       (any? true? [1 false nil])
       (any? true? [])
       (any? true? nil)))

(deftest any-true?-test
  (are [x] (true? x)
       (any-true? [true false nil])
       (any-true? [1 false nil]))
  (are [x] (false? x)
       (any-true? [false nil])
       (any-true? [nil])
       (any-true? [])
       (any-true? nil)))

(deftest |>-test
  (is (= 42 (|> 7 #(* 3 %) (partial * 2))))
  (is (= 4 (|> 4)))
  (is (= [] (|> [])))
  (is (= nil (|> nil)))
  (is (function? (|> #())))
  (is (thrown? clojure.lang.ArityException (|>))))
