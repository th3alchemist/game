(ns game.connect-four)

(require 'matrix.core)
(alias 'mCore 'matrix.core)

(def c4-chars {:wm (char \u26C0)
               :bm (char \u26C2)})

(def windows-c4-chars {:wm (char 2)
                       :bm (char 1)})

(def new-c4-baord
  (mCore/matrix 7))

(def player-token {true :wm false :bm})

(defn full? [board]
  (not-any? nil? board))

(defn lowest-row [board col-index]
  (let [col (mCore/nth-col board col-index)]
    (mod
      (dec (.indexOf col (first (filter (complement nil?) col))))
      (:row_cnt (meta board)))))

(defn down-right [board row col]
  (loop [i 0 rtn []]
    (if (or (= (+ row i) (:row_cnt (meta board)))
            (= (+ col i) (:row_cnt (meta board))))
      rtn
      (recur (inc i) (conj rtn (nth board (mCore/get-pos board [(+ row i) (+ col i)])))))))

(defn up-left [board row col]
  (loop [i 0 rtn []]
    (if (or (< (- row i) 0)
            (< (- col i) 0))
      rtn
      (recur (inc i) (conj rtn (nth board (mCore/get-pos board [(- row i) (- col i)])))))))

(defn up-right [board row col]
  (loop [i 0 rtn []]
    (if (or (< (- row i) 0)
            (= (+ col i) (:row_cnt (meta board))))
      rtn
      (recur (inc i) (conj rtn (nth board (mCore/get-pos board [(- row i) (+ col i)])))))))

(defn down-left [board row col]
  (loop [i 0 rtn []]
    (if (or (= (+ row i) (:row_cnt (meta board)))
            (< (- col i) 0))
      rtn
      (recur (inc i) (conj rtn (nth board (mCore/get-pos board [(+ row i) (- col i)])))))))

(defn get-diagonals [board row col]
  (filter #(< 3 (count %)) (list
    (concat (reverse (up-left board row col)) (rest (down-right board row col)))
    (concat (reverse (up-right board row col)) (rest (down-left board row col))))))

(defn get-diagonal-quads [dia-vec]
  (loop [i 0 rtn []]
    (if (> (+ i 4) (count dia-vec))
      (filter #(not-any? nil? %) rtn)
      (recur (inc i)
             (conj rtn
                   (take 4 (drop i dia-vec)))))))

(defn get-quads [row-vec col-vec]
  (loop [i 0 rtn []]
    (if (= i 4)
      (filter #(not-any? nil? %) rtn)
      (recur (inc i)
             (conj rtn
                   (take 4 (drop i row-vec))
                   (take 4 (drop i col-vec)))))))

(defn winner [board row col]
  (let [quad-lst (concat (get-diagonal-quads (first (get-diagonals board row col)))
                         (get-diagonal-quads (last  (get-diagonals board row col)))
                         (get-quads (mCore/nth-row board row) (mCore/nth-col board col)))]
    (cond
      (not (empty? (filter #(every? #{:bm} %) quad-lst))) "bm"
      (not (empty? (filter #(every? #{:wm} %) quad-lst))) "wm"
      :else nil)))

(defn c4
  ([]
    (c4 true (mCore/matrix 7)))
  ([player board]
    (println (str " 1    2    3    4    5    6    7\n"
                  (mCore/matrix-str (replace windows-c4-chars board))
                  "\n\n"
                  (name (player-token player))
                  " enter a number: "))
    (let [col (dec (. Integer parseInt (read-line)))
          row (lowest-row board col)
          new-board (mCore/matrix-assoc board row col (player-token player))]
      (cond (winner new-board row col) (print (str (name (player-token player))
                                                   " wins\n"
                                                   (mCore/matrix-str (replace windows-c4-chars new-board))))
            (full? board) (println "game over.")
            :else (recur (not player)
                         new-board)))))