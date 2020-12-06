(ns gops.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn initial-state
  "Create the initial state"
  [num-cards]
  [{
   :bounty (vec (range 0 num-cards))
   :p1 {:deck (vec (range 0 num-cards)) :bounty 0}
   :p2 {:deck (vec (range 0 num-cards)) :bounty 0}
   }]
  )

(defn without [c v] (remove #(= % v) c))

(defn next-state
  "generate the next state given a play"
  [s p1 p2 bounty]
  (cond (= p1 p2) (-> s
                      (update-in [:bounty] without bounty)
                      (update-in [:p2 :deck] without p2)
                      (update-in [:p1 :deck] without p1)
                      )
        (< p1 p2) (-> s
                      (update-in [:bounty] without bounty)
                      (update-in [:p2 :deck] without p2)
                      (update-in [:p1 :deck] without p1)
                      (update-in [:p2 :bounty] + bounty)
                      )
        (> p1 p2) (-> s
                      (update-in [:bounty] without bounty)
                      (update-in [:p2 :deck] without p2)
                      (update-in [:p1 :deck] without p1)
                      (update-in [:p1 :bounty] + bounty)
                      )
        )
  )

(defn end?
  "return true if the game is done"
  [s]
  (= (get s :bounty) ())
  )

(defn choose-random
  "choose a random element"
  [s p]
  (rand-nth (get-in s [p :deck]))
  )

(defn choose-same
  "choose the same element"
  [s card]
  card
  )

(defn turn
  "play a turn by using each players strategy"
  [s]
  (let
      [p1-deck (get-in s [:p1 :deck])
       p2-deck (get-in s [:p2 :deck])
       bounty (rand-nth (get-in s [:bounty]))]
       (next-state s (choose-random s :p1) (choose-same s bounty) bounty)))

(defn playGame
  "Play the game"
  [states]
  (if (end? (last states))
    states
    (playGame (conj states (turn (last states)))))
  )

(defn toConsole
  "log each turn"
  [initialState nextState]
  (print (format "bounty: %d " (clojure.set/difference (get initialState :bounty) (get nextState :bounty))))
  )
