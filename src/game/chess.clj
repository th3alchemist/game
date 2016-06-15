(ns game.chess)

(require 'matrix.core)
(alias 'mCore 'matrix.core)


(def new-chess-board
  (with-meta
    [:wr :wh :wb :wk :wq :wb :wh :wr
     :wp :wp :wp :wp :wp :wp :wp :wp
     nil nil nil nil nil nil nil nil
     nil nil nil nil nil nil nil nil
     nil nil nil nil nil nil nil nil
     nil nil nil nil nil nil nil nil
     :bp :bp :bp :bp :bp :bp :bp :bp
     :br :bh :bb :bk :bq :bb :bh :br]
    {:row_cnt 8
     :col_cnt 8
     :row_names [:a :b :c :d :e :f :g :h]
     :col_names [:a :b :c :d :e :f :g :h]}))

(def chess-chars {:wk (char \u2654)
                  :wq (char \u2655)
                  :wr (char \u2656)
                  :wb (char \u2657)
                  :wh (char \u2658)
                  :wp (char \u2659)
                  :bk (char \u265A)
                  :bq (char \u265B)
                  :br (char \u265C)
                  :bb (char \u265D)
                  :bh (char \u265E)
                  :bp (char \u265F)})