package cn.edu.ruc.hola.utils

import java.util.*

//https://leetcode.cn/problems/combinations/solution/hui-su-suan-fa-jian-zhi-python-dai-ma-java-dai-ma-/
object Combination {
    private var _cnt = 0

    fun combine(n: Int, k: Int): Int {
        _cnt = 0
        if (k <= 0 || n < k) {
            return _cnt
        }
        // 为了防止底层动态数组扩容，初始化的时候传入最大长度
        val path: Deque<Int> = ArrayDeque(k)
        dfs(1, n, k, path)
        return _cnt
    }

    private fun dfs(begin: Int, n: Int, k: Int, path: Deque<Int>) {
        if (k == 0) {
            _cnt++
            return
        }

        // 基础版本的递归终止条件：if (begin == n + 1) {
        if (begin > n - k + 1) {
            return
        }
        // 不选当前考虑的数 begin，直接递归到下一层
        dfs(begin + 1, n, k, path)

        // 不选当前考虑的数 begin，递归到下一层的时候 k - 1，这里 k 表示还需要选多少个数
        path.addLast(begin)
        dfs(begin + 1, n, k - 1, path)
        // 深度优先遍历有回头的过程，因此需要撤销选择
        path.removeLast()
    }
}