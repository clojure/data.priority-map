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
      (conj p {:b 6}) (conj h {:b 6})
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
      (meta (with-meta p {:extra :info})) {:extra :info}
      (peek p) [:b 1]
      (pop p) {:a 2 :c 3 :f 3 :e 4 :d 5}
      (peek (priority-map)) nil
      (seq (priority-map-by (comparator >) :a 1 :b 2 :c 3)) [[:c 3] [:b 2] [:a 1]])))

(deftest test-priority-map-with-flexible-order
  ;Note when implementation of hash-set changed,
  ;we need to consider that the :c and :f entries might be swapped
  (let [p (priority-map :a 2 :b 1 :c 3 :d 5 :e 4 :f 3)
        h {:a 2 :b 1 :c 3 :d 5 :e 4 :f 3}]
    (are [x y z] (or (= x y) (= x z))
         (seq p) '([:b 1] [:a 2] [:c 3] [:f 3] [:e 4] [:d 5])
                 '([:b 1] [:a 2] [:f 3] [:c 3] [:e 4] [:d 5])
         (rseq p) '([:d 5] [:e 4] [:c 3] [:f 3] [:a 2] [:b 1])
                  '([:d 5] [:e 4] [:f 3] [:c 3] [:a 2] [:b 1])
         (rest p) '([:a 2] [:c 3] [:f 3] [:e 4] [:d 5])
                  '([:a 2] [:f 3] [:c 3] [:e 4] [:d 5]))))
         
(deftest test-priority-map-keyfn
  (let [p (priority-map-keyfn first :a [2 :a] :b [1 :b] :c [3 :c] :d [5 :d] :e [4 :e] :f [3 :f])
        h {:a [2 :a] :b [1 :b] :c [3 :c] :d [5 :d] :e [4 :e] :f [3 :f]}]
    (are [x y] (= x y)
      p h
      h p
      (.hashCode p) (.hashCode h)
      (assoc p :g [1 :g]) (assoc h :g [1 :g])
      (assoc p :g [0 :g]) (assoc h :g [0 :g])
      (assoc p :c [4 :c]) (assoc h :c [4 :c])
      (assoc p :c [6 :c]) (assoc h :c [6 :c])
      (assoc p :b [2 :b]) (assoc h :b [2 :b])
      (assoc p :b [6 :b]) (assoc h :b [6 :b])
      (dissoc p :e) (dissoc h :e)
      (dissoc p :g) (dissoc h :g)
      (dissoc p :c) (dissoc h :c)
      (dissoc p :x) p
      (peek (dissoc p :x)) (peek p)
      (pop (dissoc p :x)) (pop p)
      (conj p [:g [1 :g]]) (conj h [:g [1 :g]])
      (conj p [:g [0 :g]]) (conj h [:g [0 :g]])
      (conj p [:c [4 :c]]) (conj h [:c [4 :c]])
      (conj p [:c [6 :c]]) (conj h [:c [6 :c]])
      (conj p [:b [2 :b]]) (conj h [:b [2 :b]])
      (conj p [:b [6 :b]]) (conj h [:b [6 :b]])
      (into p [[:g [0 :g]] [:h [1 :h]] [:i [2 :i]]]) (into h [[:g [0 :g]] [:h [1 :h]] [:i [2 :i]]])
      (count p) (count h)
      (empty? p) false
      (empty? (priority-map-keyfn first)) true
      (contains? p :a) true
      (contains? p :g) false
      (get p :a) [2 :a]
      (get p :a 8) [2 :a]
      (get p :g) nil
      (get p :g 8) 8
      (p :a) [2 :a]
      (:a p) [2 :a]
      (first p) [:b [1 :b]]
      (meta (with-meta p {:extra :info})) {:extra :info}
      (peek p) [:b [1 :b]]
      (pop p) {:a [2 :a] :c [3 :c] :f [3 :f] :e [4 :e] :d [5 :d]}
      (into (empty (priority-map-by >)) [[:a 2] [:b 1] [:c 3] [:d 5] [:e 4] [:f 3]])
      {:d 5, :e 4, :c 3, :f 3, :a 2, :b 1}
      (peek (priority-map-keyfn first)) nil
      (seq (into (empty (priority-map-keyfn-by first (comparator >)))  [[:a [1 :a]] [:b [2 :b]] [:c [3 :c]]]))
      '([:c [3 :c]] [:b [2 :b]] [:a [1 :a]])
      (seq (priority-map-keyfn-by first (comparator >) :a [1 :a] :b [2 :b] :c [3 :c])) [[:c [3 :c]] [:b [2 :b]] [:a [1 :a]]])))

(deftest test-priority-map-keyfn-with-flexible-order
  ;Note when implementation of hash-set changed,
  ;we need to consider that the :c and :f entries might be swapped
  (let [p (priority-map-keyfn first :a [2 :a] :b [1 :b] :c [3 :c] :d [5 :d] :e [4 :e] :f [3 :f])
        h {:a [2 :a] :b [1 :b] :c [3 :c] :d [5 :d] :e [4 :e] :f [3 :f]}]
    (are [x y z] (or (= x y) (= x z))
         (seq p) '([:b [1 :b]] [:a [2 :a]] [:c [3 :c]] [:f [3 :f]] [:e [4 :e]] [:d [5 :d]])
                 '([:b [1 :b]] [:a [2 :a]] [:f [3 :f]] [:c [3 :c]] [:e [4 :e]] [:d [5 :d]])
         (rseq p) '([:d [5 :d]] [:e [4 :e]] [:c [3 :c]] [:f [3 :f]] [:a [2 :a]] [:b [1 :b]])
                  '([:d [5 :d]] [:e [4 :e]] [:f [3 :f]] [:c [3 :c]] [:a [2 :a]] [:b [1 :b]])
         (rest p) '([:a [2 :a]] [:c [3 :c]] [:f [3 :f]] [:e [4 :e]] [:d [5 :d]])
                  '([:a [2 :a]] [:f [3 :f]] [:c [3 :c]] [:e [4 :e]] [:d [5 :d]]))))
