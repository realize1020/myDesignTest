package com.example.design.leetcode.二叉树.平衡二叉树;

public class Code01_AVLTreeMap {

	public static class AVLNode<K extends Comparable<K>, V> {
		public K k;
		public V v;
		public AVLNode<K, V> l;
		public AVLNode<K, V> r;
		public int h;

		public AVLNode(K key, V value) {
			k = key;
			v = value;
			h = 1;
		}
	}

	public static class AVLTreeMap<K extends Comparable<K>, V> {
		private AVLNode<K, V> root;
		private int size;

		public AVLTreeMap() {
			root = null;
			size = 0;
		}

		private AVLNode<K, V> rightRotate(AVLNode<K, V> cur) {
			AVLNode<K, V> left = cur.l;
			cur.l = left.r; //当前结点的左指向左子树的右节点
			left.r = cur; //左子树的右节点指向当前结点
			//节点调整后的高度
			cur.h = Math.max((cur.l != null ? cur.l.h : 0), (cur.r != null ? cur.r.h : 0)) + 1;//计算当前结点的高度
			left.h = Math.max((left.l != null ? left.l.h : 0), (left.r != null ? left.r.h : 0)) + 1;//计算左节点的高度
			return left;
		}

		private AVLNode<K, V> leftRotate(AVLNode<K, V> cur) {
			AVLNode<K, V> right = cur.r;
			cur.r = right.l;
			right.l = cur;
			cur.h = Math.max((cur.l != null ? cur.l.h : 0), (cur.r != null ? cur.r.h : 0)) + 1;
			right.h = Math.max((right.l != null ? right.l.h : 0), (right.r != null ? right.r.h : 0)) + 1;
			return right;
		}

		private AVLNode<K, V> maintain(AVLNode<K, V> cur) {
			if (cur == null) {
				return null;
			}
			int leftHeight = cur.l != null ? cur.l.h : 0;
			int rightHeight = cur.r != null ? cur.r.h : 0;
			if (Math.abs(leftHeight - rightHeight) > 1) {
				if (leftHeight > rightHeight) {
					int leftLeftHeight = cur.l != null && cur.l.l != null ? cur.l.l.h : 0;
					int leftRightHeight = cur.l != null && cur.l.r != null ? cur.l.r.h : 0;
					if (leftLeftHeight >= leftRightHeight) {
						cur = rightRotate(cur);// LL型：直接右旋当前节点
					} else {
						// LR型：先左旋左子树，再右旋当前节点
						cur.l = leftRotate(cur.l);// 左子树左旋
						cur = rightRotate(cur);// 当前节点右旋
					}
				} else {
					int rightLeftHeight = cur.r != null && cur.r.l != null ? cur.r.l.h : 0;
					int rightRightHeight = cur.r != null && cur.r.r != null ? cur.r.r.h : 0;
					if (rightRightHeight >= rightLeftHeight) {
						cur = leftRotate(cur);
					} else {
						cur.r = rightRotate(cur.r);
						cur = leftRotate(cur);
					}
				}
			}
			return cur;
		}

		/**
		 * 找到就返回，找不到就返回离的最近的节点
		 * 使用pre节点的优势：
		 * 当要查找的 key 在树中不存在时，cur 最终会变成 null，但我们仍然需要知道搜索路径上最后一个访问的有效节点。
		 * @param key
		 * @return
		 */
		private AVLNode<K, V> findLastIndex(K key) {
			AVLNode<K, V> pre = root;
			AVLNode<K, V> cur = root;
			while (cur != null) {
				pre = cur;
				if (key.compareTo(cur.k) == 0) {
					break;
				} else if (key.compareTo(cur.k) < 0) {
					cur = cur.l;
				} else {
					cur = cur.r;
				}
			}
			return pre;
		}

		private AVLNode<K, V> findLastNoSmallIndex(K key) {
			AVLNode<K, V> ans = null;
			AVLNode<K, V> cur = root;
			while (cur != null) {
				if (key.compareTo(cur.k) == 0) {
					ans = cur;
					break;
				} else if (key.compareTo(cur.k) < 0) {
					ans = cur;
					cur = cur.l;
				} else {
					cur = cur.r;
				}
			}
			return ans;
		}

		private AVLNode<K, V> findLastNoBigIndex(K key) {
			AVLNode<K, V> ans = null;
			AVLNode<K, V> cur = root;
			while (cur != null) {
				if (key.compareTo(cur.k) == 0) {
					ans = cur;
					break;
				} else if (key.compareTo(cur.k) < 0) {
					cur = cur.l;
				} else {
					ans = cur;
					cur = cur.r;
				}
			}
			return ans;
		}

		/**
		 *  3. 使用`cur.h++`（即当前节点的高度加1）是不正确的，因为：
		 *     - 插入节点后，当前节点的高度可能不变！例如，当前节点的左子树高度为2，右子树高度为1，那么当前节点高度为max(2,1)+1=3。现在，如果我们在左子树上插入一个节点，但插入后左子树的高度可能仍然是2（因为插入在较矮的子树中），那么当前节点的高度应该仍然是3，而不是增加1。
		 *     - 只有当我们插入节点导致子树高度增加，并且这个增加使得当前节点的左右子树中较高的那一侧高度增加时，当前节点的高度才会增加。但即使增加，也不一定是+1，因为当前节点的高度是由左右子树中较高的那个高度加1得到的，所以如果左子树高度增加后仍然小于或等于右子树高度，那么当前节点的高度可能不变。
		 *  4. 因此，我们不能简单地使用`cur.h++`，因为当前节点的高度取决于其左右子树的高度。我们需要重新计算：取左右子树高度的最大值，然后加1。
		 * @param cur
		 * @param key
		 * @param value
		 * @return
		 */
		private AVLNode<K, V> add(AVLNode<K, V> cur, K key, V value) {
			if (cur == null) {
				return new AVLNode<K, V>(key, value);
			} else {
				if (key.compareTo(cur.k) < 0) {
					cur.l = add(cur.l, key, value);
				} else {
					cur.r = add(cur.r, key, value);
				}
				cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;//重新计算树的高度
				return maintain(cur);
			}
		}

		// 在cur这棵树上，删掉key所代表的节点
		// 返回cur这棵树的新头部
		private AVLNode<K, V> delete(AVLNode<K, V> cur, K key) {
			if (cur == null) {  // 需要添加这个检查
				return null;    // key不存在，直接返回
			}
			if (key.compareTo(cur.k) > 0) {
				cur.r = delete(cur.r, key);
			} else if (key.compareTo(cur.k) < 0) {
				cur.l = delete(cur.l, key);
			} else {
				if (cur.l == null && cur.r == null) {
					cur = null;
				} else if (cur.l == null && cur.r != null) {
					cur = cur.r;
				} else if (cur.l != null && cur.r == null) {
					cur = cur.l;
				} else {
					AVLNode<K, V> des = cur.r;// 找到右子树
					while (des.l != null) {// 找到右子树中的最小节点（中序后继）
						des = des.l;
					}
					cur.r = delete(cur.r, des.k);// 从右子树中删除这个后继节点
					des.l = cur.l;// 将原节点的左子树给后继节点
					des.r = cur.r; // 将原节点的右子树给后继节点
					cur = des;// 用后继节点替换原节点
				}
			}
			if (cur != null) {//删除完计算高度
				cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;
			}
			return maintain(cur);
		}

		public int size() {
			return size;
		}

		public boolean containsKey(K key) {
			if (key == null) {
				return false;
			}
			AVLNode<K, V> lastNode = findLastIndex(key);
			// 关键判断：节点存在 且 key相等
			return lastNode != null && key.compareTo(lastNode.k) == 0 ? true : false;
		}

		public void put(K key, V value) {
			if (key == null) {
				return;
			}
			AVLNode<K, V> lastNode = findLastIndex(key);//找到离的最近的节点或者就是目标节点
			if (lastNode != null && key.compareTo(lastNode.k) == 0) {//如果是目标节点，替换value
				lastNode.v = value;
			} else {//不是目标节点则新增
				size++;
				root = add(root, key, value);
			}
		}

		public void remove(K key) {
			if (key == null) {
				return;
			}
			if (containsKey(key)) {
				size--;
				root = delete(root, key);
			}
		}

		public V get(K key) {
			if (key == null) {
				return null;
			}
			AVLNode<K, V> lastNode = findLastIndex(key);
			if (lastNode != null && key.compareTo(lastNode.k) == 0) {
				return lastNode.v;
			}
			return null;
		}

		public K firstKey() {
			if (root == null) {
				return null;
			}
			AVLNode<K, V> cur = root;
			while (cur.l != null) {
				cur = cur.l;
			}
			return cur.k;
		}

		public K lastKey() {
			if (root == null) {
				return null;
			}
			AVLNode<K, V> cur = root;
			while (cur.r != null) {
				cur = cur.r;
			}
			return cur.k;
		}

		public K floorKey(K key) {
			if (key == null) {
				return null;
			}
			AVLNode<K, V> lastNoBigNode = findLastNoBigIndex(key);
			return lastNoBigNode == null ? null : lastNoBigNode.k;
		}

		public K ceilingKey(K key) {
			if (key == null) {
				return null;
			}
			AVLNode<K, V> lastNoSmallNode = findLastNoSmallIndex(key);
			return lastNoSmallNode == null ? null : lastNoSmallNode.k;
		}

	}

}
