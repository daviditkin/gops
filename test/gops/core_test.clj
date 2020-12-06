(ns gops.core-test
  (:require [clojure.test :refer :all]
            [gops.core :refer :all]))

(deftest initial-state-simple
  (testing "initial state"
    (is (= [0 1 2 3 4] (get-in (gops.core/initial-state 5) [:p1 :deck])))))

