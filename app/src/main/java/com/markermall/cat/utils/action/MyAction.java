package com.markermall.cat.utils.action;

/**
 * 封装一个没有返回值的方法。
 */
public class MyAction {
    /**
     * 封装一个没有参数且没有返回值的方法。
     * <p>
     * <pre class="prettyprint">
     * public class MyActionVoid extends Void {
     * <p>
     * public MyActionVoid() {
     * new MyAction().super();
     * }
     * <p>
     * public MyActionVoid(MyAction action) {
     * action.super();
     * }
     * <p>
     * &#064;Override
     * public void invoke() {
     * ...
     * }
     * }
     * </pre>
     */
    public abstract class Void {
        /**
         * 执行方法。
         */
        public abstract void invoke();
    }

    /**
     * 封装一个具有一个参数且没有返回值的方法。
     * <p>
     * <pre class="prettyprint">
     * public class MyActionOne&lt;T&gt; extends MyAction.One&lt;T&gt; {
     * public MyActionOne() {
     * new MyAction().super();
     * }
     * <p>
     * public MyActionOne(MyAction action) {
     * action.super();
     * }
     * <p>
     * &#064;Override
     * public void invoke(T arg) {
     * ...
     * }
     * }
     * </pre>
     *
     * @param <T>：此委托封装的方法的参数类型。
     */
    public abstract static class One<T> {
        /**
         * 执行方法。
         *
         * @param arg ：此委托封装的方法的参数。
         */
        public abstract void invoke(T arg);
    }

    /**
     * 封装一个具有两个参数且没有返回值的方法。
     * <p>
     * <pre class="prettyprint">
     * public class MyActionTwo&lt;T1, T2&gt; extends MyAction.Two&lt;T1, T2&gt; {
     * public MyActionTwo() {
     * new MyAction().super();
     * }
     * <p>
     * public MyActionTwo(MyAction action) {
     * action.super();
     * }
     * <p>
     * &#064;Override
     * public void invoke(T1 arg1, T2 arg2) {
     * ...
     * }
     * }
     * </pre>
     *
     * @param <T1>：此委托封装的方法的第一个参数类型。
     * @param <T2>：此委托封装的方法的第二个参数类型。
     */
    public abstract static class Two<T1, T2> {
        /**
         * 执行方法。
         *
         * @param arg1 ：此委托封装的方法的第一个参数。
         * @param arg2 ：此委托封装的方法的第二个参数。
         */
        public abstract void invoke(T1 arg1, T2 arg2);
    }

    public abstract static class OnResult<T> {
        /**
         * 执行方法。
         *
         * @param arg ：此委托封装的方法的参数。
         */
        public abstract void invoke(T arg);

        /**
         * 返回错误
         *
         * @param
         */
        public abstract void onError();
    }
    public abstract static class OnResultFinally  <T> extends OnResult<T> {


        /**
         * 结束
         *
         * @param
         */
        public abstract void onFinally();
    }

    public abstract static class OnResultTwo<T,T1> {
        /**
         * 执行方法。
         *
         * @param arg ：此委托封装的方法的参数。
         */
        public abstract void invoke(T arg,T1 arg1);

        /**
         * 返回错误
         *
         * @param
         */
        public abstract void onError();
    }

    /**
     * 封装一个具有三个参数且没有返回值的方法。
     * <p>
     * <pre class="prettyprint">
     * public class MyActionThree&lt;T1, T2, T3&gt; extends MyAction.Three&lt;T1, T2, T3&gt; {
     * public MyActionThree() {
     * new MyAction().super();
     * }
     * <p>
     * public MyActionThree(MyAction action) {
     * action.super();
     * }
     * <p>
     * &#064;Override
     * public void invoke(T1 arg1, T2 arg2, T3 arg3) {
     * ...
     * }
     * }
     * </pre>
     *
     * @param <T1>：此委托封装的方法的第一个参数类型。
     * @param <T2>：此委托封装的方法的第二个参数类型。
     * @param <T3>：此委托封装的方法的第三个参数类型。
     */
    public abstract class Three<T1, T2, T3> {
        /**
         * 执行方法。
         *
         * @param arg1 ：此委托封装的方法的第一个参数。
         * @param arg2 ：此委托封装的方法的第二个参数。
         * @param arg3 ：此委托封装的方法的第三个参数。
         */
        public abstract void invoke(T1 arg1, T2 arg2, T3 arg3);
    }

    /**
     * 封装一个具有四个参数且没有返回值的方法。
     * <p>
     * <pre class="prettyprint">
     * public class MyActionFour&lt;T1, T2, T3, T4&gt; extends MyAction.Four&lt;T1, T2, T3, T4&gt; {
     * public MyActionFour() {
     * new MyAction().super();
     * }
     * <p>
     * public MyActionFour(MyAction action) {
     * action.super();
     * }
     * <p>
     * &#064;Override
     * public void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
     * ...
     * }
     * }
     * </pre>
     *
     * @param <T1>：此委托封装的方法的第一个参数类型。
     * @param <T2>：此委托封装的方法的第二个参数类型。
     * @param <T3>：此委托封装的方法的第三个参数类型。
     * @param <T4>：此委托封装的方法的第四个参数类型。
     */
    public abstract class Four<T1, T2, T3, T4> {
        /**
         * 执行方法。
         *
         * @param arg1 ：此委托封装的方法的第一个参数。
         * @param arg2 ：此委托封装的方法的第二个参数。
         * @param arg3 ：此委托封装的方法的第三个参数。
         * @param arg4 ：此委托封装的方法的第四个参数。
         */
        public abstract void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4);
    }

    /**
     * 封装一个具有五个参数且没有返回值的方法。
     * <p>
     * <pre class="prettyprint">
     * public class MyActionFour&lt;T1, T2, T3, T4, T5&gt; extends MyAction.Four&lt;T1, T2, T3, T4, T5&gt; {
     * public MyActionFour() {
     * new MyAction().super();
     * }
     * <p>
     * public MyActionFour(MyAction action) {
     * action.super();
     * }
     * <p>
     * &#064;Override
     * public void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) {
     * ...
     * }
     * }
     * </pre>
     *
     * @param <T1>：此委托封装的方法的第一个参数类型。
     * @param <T2>：此委托封装的方法的第二个参数类型。
     * @param <T3>：此委托封装的方法的第三个参数类型。
     * @param <T4>：此委托封装的方法的第四个参数类型。
     * @param <T5>：此委托封装的方法的第五个参数类型。
     */
    public abstract class Five<T1, T2, T3, T4, T5> {
        /**
         * 执行方法。
         *
         * @param arg1 ：此委托封装的方法的第一个参数。
         * @param arg2 ：此委托封装的方法的第二个参数。
         * @param arg3 ：此委托封装的方法的第三个参数。
         * @param arg4 ：此委托封装的方法的第四个参数。
         * @param arg5 ：此委托封装的方法的第五个参数。
         */
        public abstract void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5);
    }
}