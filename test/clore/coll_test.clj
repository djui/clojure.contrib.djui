(ns clore.coll-test
  (:use clojure.test clore.coll))


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

(deftest gapmap-test
  (are [x y] (= x y)
       [1 2 1] (gapmap + [0 1 1 0])
       [2]     (gapmap + [1 1])
       [1]     (gapmap + [1])
       []      (gapmap + [])
       nil     (gapmap + nil)))
