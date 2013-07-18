(ns clojure.contrib.djui.io-test
  (:use clojure.contrib.djui.io
        clojure.test))


(deftest read-file-test
  (let [filepath (tempfile)]
    (is (= nil (read-file filepath)))
    (spit filepath "1")
    (is (= 1 (read-file filepath)))
    (spit filepath "#=(prn \"foo\")")
    (is (thrown? java.lang.RuntimeException #"No dispatch macro for"
                 (read-file filepath)))
    (spit filepath "#java.io.FileWriter[\"foobar.txt\"]") 
    (is (thrown? java.lang.RuntimeException #"No reader function for" (read-file filepath)))))
