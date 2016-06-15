(ns cards.core)

(def suits [:clubs :hearts :spades :diamonds])
(def windows-suit-chars {:big-joker (char 1) :little-joker (char 2) :hearts (char 3) :diamonds (char 4) :clubs (char 5) :spades (char 6)})
(def suit-chars {:spades (char \u2660) :hearts (char \u2661) :diamonds (char \u2662) :clubs (char \u2663) :little-joker (char \u263A) :big-joker (char \u263B)})

(def suit-colors {:hearts :red :diamonds :red :clubs :black :spades :black})
(def ranks (range 1 14))
(def rank-names {0 "joker" 1 "ace" 11 "jack" 12 "queen" 13 "king"})
(def short-rank-names {0 "*" 1 "A" 11 "J" 12 "Q" 13 "K"})

(def new-deck (for [r ranks s suits] [r s]))

(def new-joker-deck (concat new-deck [[0 :little-joker] [0 :big-joker]]))

(def new-chased-deck (for [i (range 0 52)]
                        (let [rank (inc (mod (* i 3) (count ranks)))
                              suit (get suits (mod i (count suits)))]
                          [rank suit])))

(defn same-suit? [[_ s1] [_ s2]] (= s1 s2))

(defn same-rank? [[r1 _] [r2 _]] (= r1 r2))

(defn red-card? [[_ suit]] (= :red (suit-colors suit)))

(defn black-card? [[_ suit]] (= :black (suit-colors suit)))

(defn color [[_ suit]] (suit-colors suit))

(defn rank [[rank _]] rank)

(defn suit [[_ suit]] suit)

(defn cut [deck n]
  (let [[top bottom] (split-at (mod n (count deck)) deck)]
    (concat bottom top)))

(defn draw [[card & remaining-deck]]
  (list card remaining-deck))

(defn card-take [n deck]
  [(take n deck)
  (remove (set (take n deck)) deck)])

(defn card-str [[rank suit]]
  (str (if (nil? (rank-names rank))
           rank
           (rank-names rank))
       " of "
       (suit windows-suit-chars)))

(defn short-card-str [[rank suit]]
  (str (if (nil? (rank-names rank))
           rank
           (short-rank-names rank))
       (suit windows-suit-chars)))

(defn deck-str [deck del & [length _]]
  ":length is :long or :short"
  (apply str (interleave (map ({:long card-str} length short-card-str)
                              deck)
                         (repeat (count deck) del))))