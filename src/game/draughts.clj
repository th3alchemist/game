(ns game.draughts)

(require 'matrix.core)
(alias 'mCore 'matrix.core)


(def new-draughts-board
  (with-meta
    [nil :wm nil :wm nil :wm nil :wm
     :wm nil :wm nil :wm nil :wm nil
     nil :wm nil :wm nil :wm nil :wm
     nil nil nil nil nil nil nil nil
     nil nil nil nil nil nil nil nil
     :bm nil :bm nil :bm nil :bm nil
     nil :bm nil :bm nil :bm nil :bm
     :bm nil :bm nil :bm nil :bm nil]
    {:row_cnt 8
     :col_cnt 8
     :row_names [:a :b :c :d :e :f :g :h]
     :col_names [:a :b :c :d :e :f :g :h]}))

(def draughts-chars {:wm (char \u26C0)
                     :wk (char \u26C1)
                     :bm (char \u26C2)
                     :bk (char \u26C3)})