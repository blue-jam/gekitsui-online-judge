#### 問題文

2つの整数\\(A\\), \\(B\\)が与えられるので、\\(A + B\\)を\\(10\\)で割ったあまりを求めてください。

---

#### 制約

- \\(0 \le A, B \le 9\\)

#### 入力

入力は以下の形式でなければならない。

<div class="InputFormat">
\(A\) \(B\)
</div>

\\(A\\)と\\(B\\)の間は半角スペース1つで区切られている。入力末尾の改行を忘れないこと。

---

#### コード

太郎くんは次のKotlinコードを提出しました。

```kotlin
fun main() {
    val sc = Scanner(System.`in`)
    val a = sc.nextInt()
    val b = sc.nextInt()
    println(a + b)
}
```
