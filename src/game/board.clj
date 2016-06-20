(ns game.board)

(require 'matrix.core)
(alias 'mCore 'matrix.core)

(defn full? [board]
  (not-any? nil? board))

(defn xor [a b]
  (or (and a (not b))
      (and (not a) b)))

(def tile-chars {:black (char \u2580) :white (char \u2581)})

(def color {true :black false :white})

(defn tile-color [board pos]
  (let [row (mCore/get-row board pos)
        col (mCore/get-col board pos)]
    (color (xor (even? row)
                (even? col)))))

(defn new-board [n]
  (mCore/matrix n))

(defn new-colored-board [n]
  (loop [i 0 board (mCore/matrix n)]
    (if (= i (* n n))
      board
      (recur
        (inc i)
        (mCore/matrix-assoc board
                      i
                      (tile-color board i))))))

(defn place-token [board pos token]
  (mCore/matrix-assoc board pos token))

(defn move-token [board src dest]
  (let [token (nth board src)]
    (mCore/matrix-assoc 
      (mCore/matrix-assoc board dest token)
      src
      nil)))

(defn board-str [board & [justify _]]
  (mCore/matrix-str board justify))

;(print (board-str (replace chess-chars new-chess-board)))

;(print (matrix-str (replace tile-chars (new-colored-board 8))))