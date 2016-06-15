(ns game.ttt)
(require 'matrix.core)
(alias 'mCore 'matrix.core)


(def player-token {true :x false :o})
(def value-map {0 3, 1 2, 2 3, 3 2, 4 4, 5 2, 6 3, 7 2, 8 3})
(def inf (/ 1.0 0.0))
(def neg-inf (/ -1.0 0.0))

(defn full? [board]
  (not-any? nil? board))

(defn triples [board]
  (conj (concat (mCore/all-rows board)
                (mCore/all-cols board))
        (mCore/diagonal board)
        (mCore/anti-diagonal board)))

(defn winner [board]
  (cond
    (some true? (map #(every? #{:x} %) (triples board))) "x"
    (some true? (map #(every? #{:o} %) (triples board))) "o"
    :else nil))

(defn index-vector [board]
  (loop [i 0 acc []]
    (if (>= i (count board))
      acc
      (recur (inc i)
             (conj acc
                   [i (nth board i)])))))

(defn nil-positions [board] (map first (filter #(nil? (second %)) (index-vector board))))

(defn eval-space [board pos turn]
  (let [row (mCore/nth-row board (first (mCore/get-coor board pos)))
        col (mCore/nth-col board (last (mCore/get-coor board pos)))]
    (vector 
      (if (= 2 (count (filter #{(player-token turn)} row)))
        inf
        (count (filter #{(player-token turn)} row)))
      (if (= 2 (count (filter #{(player-token turn)} col)))
        inf
        (count (filter #{(player-token turn)} col)))
      (if (= 2 (count (filter #{(player-token (not turn))} row)))
        neg-inf
        (* -1 (count (filter #{(player-token (not turn))} row))))
      (if (= 2 (count (filter #{(player-token (not turn))} col)))
        neg-inf
        (* -1 (count (filter #{(player-token (not turn))} row)))))))

(defn cpu-random [board & _]
  (first (shuffle (nil-positions board))))

(defn cpu-score [board & _]
  (key (apply max-key val
              (apply hash-map (interleave (nil-positions board)
                                          (map value-map (nil-positions board)))))))

(defn cpu-eval [board turn]
    (key (apply max-key val
                (apply hash-map (interleave (nil-positions board)
                                            (map #(apply max %)
                                                 (map #(eval-space board % true) (nil-positions board))))))))

(defn human [& _]
  (dec (. Integer parseInt (read-line))))

(def turn-map {true human false cpu-random})

(defn ttt 
  ([]
    (ttt true (mCore/matrix 3)))
  ([player board]
    (println (str (mCore/matrix-str (mCore/flip board))
                  "\n\n"
                  (name (player-token player))
                  " enter a number: "))
    (cond
      (winner board) (println (winner board) " is the winner!")
      (full? board) (println "game over.")
      :else (recur (not player) (mCore/matrix-assoc board
                                                    ((turn-map player) board player)
                                                    (player-token player))))))