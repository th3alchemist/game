(ns game.connect-four)

(require 'matrix.core)
(alias 'mCore 'matrix.core)

(def board
  (with-meta
    [:wr :wh :wb :wk :wq :wb :wh
     :wp :wp :wp :wp :wp :wp :wp
     nil nil nil nil nil nil nil
     nil nil nil nil nil nil nil
     nil nil nil nil nil nil nil
     :bp :bp :bp :bp :bp :bp :bp
     :br :bh :bb :bk :bq :bb :bh]
    {:row_cnt 7
     :col_cnt 7
     :row_names [:a :b :c :d :e :f :g :h]
     :col_names [:a :b :c :d :e :f :g :h]}))

(def c4-chars {:wm (char \u26C0)
               :bm (char \u26C2)})

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

(def quad-combinations
  (for [n (range 4) row-index (range 7)] [n row-index]))

(defn get-quad-row [board [n row-index]]
  (take 4 (drop n (mCore/nth-row board row-index))))

(defn get-quad-col [board [n col-index]]
  (take 4 (drop n (mCore/nth-col board col-index))))

(defn quadruples [board]
  (filter #(not-any? nil? %)
          (concat (map #(get-quad-row board %) quad-combinations)
                  (map #(get-quad-col board %) quad-combinations))))
                  ;;get diagonals

(defn winner [board]
  (cond
    (not (empty? (filter #(every? #{:bm} %) (quadruples board)))) "bm"
    (not (empty? (filter #(every? #{:wm} %) (quadruples board)))) "wm"
    :else nil))

(defn c4 
  ([]
    (c4 true (mCore/matrix 7)))
  ([player board]
    (println (str " 1    2    3    4    5    6    7\n"
                  (mCore/matrix-str board)
                  "\n\n"
                  (name (player-token player))
                  " enter a number: "))
    (cond (winner board) (println (name (player-token (not player))) "wins")
          (full? board) (println "game over.")
          :else (let [col (dec (. Integer parseInt (read-line)));;make sure to read-line AFTER checking for a winner
                      row (lowest-row board col)]
                  (recur (not player) (mCore/matrix-assoc board
                                                          row
                                                          col
                                                          (player-token player)))))))