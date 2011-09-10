(ns clojure.data.test-priority-map
  (:use clojure.test
	clojure.data.priority-map))

(deftest test-priority-map
  (let [p (priority-map :a 2 :b 1 :c 3 :d 5 :e 4 :f 3)
        h {:a 2 :b 1 :c 3 :d 5 :e 4 :f 3}]
    (are [x y] (= x y)
      p {:a 2 :b 1 :c 3 :d 5 :e 4 :f 3}
      h p
      (priority-map 1 2) (priority-map 1 2)
      (.hashCode p) (.hashCode {:a 2 :b 1 :c 3 :d 5 :e 4 :f 3})
      (assoc p :g 1) (assoc h :g 1)
      (assoc p :g 0) (assoc h :g 0)
      (assoc p :c 4) (assoc h :c 4)
      (assoc p :c 6) (assoc h :c 6)
      (assoc p :b 2) (assoc h :b 2)
      (assoc p :b 6) (assoc h :b 6)
      (dissoc p :e) (dissoc h :e)
      (dissoc p :g) (dissoc h :g)
      (dissoc p :c) (dissoc h :c)
      (dissoc p :x) p
      (peek (dissoc p :x)) (peek p)
      (pop (dissoc p :x)) (pop p)
      (conj p [:g 1]) (conj h [:g 1])
      (conj p [:g 0]) (conj h [:g 0])
      (conj p [:c 4]) (conj h [:c 4])
      (conj p [:c 6]) (conj h [:c 6])
      (conj p [:b 2]) (conj h [:b 2])
      (conj p [:b 6]) (conj h [:b 6])
      (into p [[:g 0] [:h 1] [:i 2]]) (into h [[:g 0] [:h 1] [:i 2]])
      (count p) (count h)
      (empty? p) false
      (empty? (priority-map)) true
      (contains? p :a) true
      (contains? p :g) false
      (get p :a) 2
      (get p :a 8) 2
      (get p :g) nil
      (get p :g 8) 8
      (p :a) 2
      (:a p) 2
      (seq p) '([:b 1] [:a 2] [:c 3] [:f 3] [:e 4] [:d 5])
      ;Note if implementation of hash-set changes, the :c and :f entries might be swapped
      (rseq p) '([:d 5] [:e 4] [:c 3] [:f 3] [:a 2] [:b 1])
      ;; (subseq p < 3) '([:b 1] [:a 2])
      ;; (subseq p <= 3) '([:b 1] [:a 2] [:c 3] [:f 3])
      ;; (subseq p > 3) '([:e 4] [:d 5])
      ;; (subseq p >= 3) '([:c 3] [:f 3] [:e 4] [:d 5])
      ;; (subseq p > 3 <= 4) '([:e 4])
      ;; (subseq p >= 3 <= 4) '([:c 3] [:f 3] [:e 4])
      ;; (subseq p >= 3 < 4) '([:c 3] [:f 3])
      ;; (subseq p > 3 < 4) nil
      ;; (subseq p > 2 < 3) nil
      ;; (subseq p > 2 <= 3) '([:c 3] [:f 3])
      ;; (subseq p >= 2 < 3) '([:a 2])
      ;; (subseq p >= 2 <= 3) '([:a 2] [:c 3] [:f 3])
      ;; (rsubseq p < 3) '([:a 2] [:b 1])
      ;; (rsubseq p <= 3) '([:c 3] [:f 3] [:a 2] [:b 1] )
      ;; (rsubseq p > 3) '([:d 5] [:e 4])
      ;; (rsubseq p >= 3) '([:d 5] [:e 4] [:c 3] [:f 3])
      ;; (rsubseq p > 3 <= 4) '([:e 4])
      ;; (rsubseq p >= 3 <= 4) '([:e 4] [:c 3] [:f 3] )
      ;; (rsubseq p >= 3 < 4) '([:c 3] [:f 3])
      ;; (rsubseq p > 3 < 4) nil
      ;; (rsubseq p > 2 < 3) nil
      ;; (rsubseq p > 2 <= 3) '([:c 3] [:f 3])
      ;; (rsubseq p >= 2 < 3) '([:a 2])
      ;; (rsubseq p >= 2 <= 3) '([:c 3] [:f 3] [:a 2] )
      (first p) [:b 1]
      (rest p) '([:a 2] [:c 3] [:f 3] [:e 4] [:d 5])
      (meta (with-meta p {:extra :info})) {:extra :info}
      (peek p) [:b 1]
      (pop p) {:a 2 :c 3 :f 3 :e 4 :d 5}
      (peek (priority-map)) nil
      (seq (priority-map-by (comparator >) :a 1 :b 2 :c 3)) [[:c 3] [:b 2] [:a 1]])))