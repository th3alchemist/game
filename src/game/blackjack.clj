(ns game.blackjack)

  loop [hand deck]
  if bj -> end
  if >21 -> end
  user input
  recur -> add card to hand

  
(defn eval-hand [[c1 c2]]
  (+ (rank c1)
     (rank c2)))

(defn blackjack [deck]
  (loop [hand (first (card-take 2 deck))
         deck (last (card-take 2 deck))]
    (print (deck-str hand ", "))
    (if (= (eval-hand hand) 21) "winner")
    (if (> (eval-hand hand) 21) "looser")
    (println "hit?")
    (if (= (read-line) "y")
      (recur [(concat (first (draw deck))
                      hand)
             (last (draw deck))]))))