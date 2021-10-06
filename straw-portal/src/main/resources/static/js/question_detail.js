let detailApp = new Vue({
    el: "#detailApp",
    data: {
        //ajax传值
        question: {}
    },
    methods: {
        loadQuestions: function () {
            //获取url路径上?之后的内容 (请求参数)
            let qid = location.search;
            console.log(qid);
            //判断qid必须存在 !qid表示如果qid不存在
            if (!qid) {
                //如果id不存在，直接报错返回
                alert("请指定问题的id");
                return;
            }
            //现在qid是?xx的格式，需要去掉?
            qid = qid.substring(1);
            $.ajax({
                url: "/v1/questions/" + qid,
                method: "GET",
                success: function (r) {
                    console.log(r);
                    if (r.code == OK) {
                        detailApp.question = r.data;
                        addDuration(detailApp.question);
                    } else {
                        alert("r.message");
                    }
                }
            })
        }

    },
    created: function () {
        this.loadQuestions();
    }
})

let postAnswerApp = new Vue({
    el: "#postAnswerApp",
    data: {
        message: "",
        hasError: false
    },
    methods: {
        postAnswer: function () {
            //获取?后面内容(请求参数?)
            let qid = location.search;
            if (!qid) {
                this.message = "必须填写问题id"
                this.hasError = true;
                return;
            }
            qid = qid.substring(1);
            //获得老师回答 的内容
            //获得富文本编译器中的value，来作为回答的内容
            let content = $("#summernote").val();//jquery代码， 获得summernote中的值
            if (!content) {
                this.message = "回答内容不能为空";
                this.hasError = true;
                return;
            }
            //获取到了qid和content 这两个即是AnswerVO中的两个属性，则可以进行声明Vo操作
            //声明一个json格式，包含id和content
            let form = {
                //json格式
                //跟AnswerVo属性对应，qid为请求参数
                questionId: qid,
                content: content
            }
            $.ajax({
                url: "/v1/answers",
                method: "POST",
                data: form,
                success: function (r) {
                    if (r.code == CREATED) {
                        //r.data是新增的Answer对象的json格式，将这个新增的answer对象添加到下面回答列表的数组中即可实现
                        //新增的同时显示在页面上
                        answerApp.answers.push(r.data);
                        r.data.duration = "刚刚";
                        //清空富文本编辑器的内容
                        $("#summernote").summernote("reset");

                        postAnswerApp.message = r.message;
                        postAnswerApp.hasError = true;
                    } else {
                        postAnswerApp.message = r.message;
                        postAnswerApp.hasError = true;
                    }
                }
            })
        }
    }

});

let answerApp = new Vue({
    el: "#answersApp",
    data: {
        answers: []
    },
    methods: {
        /*查询所有回答*/
        loadAnswers: function () {
            let qid = location.search;
            if (!qid) {
                alert("问题id不能为空");
                return;
            }
            //去掉？
            qid = qid.substring(1);
            $.ajax({
                url: "/v1/answers/question/" + qid,
                method: "get",
                success: function (r) {
                    console.log(r);
                    if (r.code == OK) {
                        answerApp.answers = r.data;
                        //计算当前查询出的所有回答的持续时间
                        answerApp.updateDuration();
                    } else {
                        alert(r.message)
                    }
                }
            })
        },
        updateDuration: function () {

            for (let i = 0; i < this.answers.length; i++) {
                addDuration(this.answers[i]);
            }
        },
        //新增评论
        postComment: function (answerId) {
            //后代选择器 id +空格+ 标签
            //获得当前回答的多行文本框对象
            let textarea = $("#addComment" + answerId + " textarea");
            //从多行文本框中获得用户编写的评论
            let content = textarea.val();
            if (!answerId || !content) {
                alert("信息不全");
                return;
            }

            //封装form对象
            let form = {
                answerId: answerId,
                content: content
            }

            console.log(form);
            $.ajax({
                url: "/v1/comments",
                method: "POST",
                data: form,
                success: function (r) {
                    if (r.code == CREATED) {
                        //清空textArea的内容
                        textarea.val("");
                        //获得要新增到页面上的评论对象
                        let comment = r.data;
                        //为了方便遍历，将answers提取成局部变量
                        let answers = answerApp.answers;
                        //遍历answers根据当前新增评论的回答
                        for (let i = 0; i < answers.length; i++) {
                            //判断当前回答和新增平路六年的回答id相等
                            if (answers[i].id == answerId) {
                                //就将新增的评论追加到当前回答的评论数组后
                                answers[i].comments.push(comment);
                                //找到对象之后退出循环
                                break;
                            }
                        }
                    } else {
                        alert(r.message);
                    }
                    console.log(r)
                }

            })
        },
        //编辑评论
        updateComment: function (commentId,answer,index) {
            //后代选择器
            let textarea = $("#editComment" + commentId + " textarea");
            let content = textarea.val();
            if (!commentId || !content) {
                alert("确认信息不能为空，谢谢");
                return;
            }
            $.ajax({
                // /v1/comments/{id}/update

                /*SpringMVC是尽量匹配原则 占位符可以放在中间*/
                url: "/v1/comments/" + commentId + "/update",
                method: "POST",
                data: {
                    content: content
                },
                success: function (r) {
                    console.log(r)
                    if (r.code == OK) {
                        //将修改的信息显示在页面上
                        //vue.set(要修改的数组,要修改的元素下标，要修改成？)
                        Vue.set(answer.comments,index,r.data)
                        //将当前修改div收起
                        $("#editComment"+commentId).collapse("hide");
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        //删除评论
        removeComment: function (commentId,answer,index) {
            if (!commentId) {
                alert("评论id不能为空");
                return;
            }
            $.ajax({
                    // /v1/comments/{id}
                    url: "/v1/comments/" + commentId + "/delete",
                    method: "GET",
                    success: function (r) {
                        console.log(r);
                        //410 用户请求的资源被删除
                        if (r.code == GONE) {
                            //alert("用户的评论已删除;" + r.message);
                            //js提供了一个从数组中删除的api 数组对象.splice(index,num)
                            //上面代码 的意思是从数组对象的index位置删除num个元素 如果只写index,会把index包括之后的元素都删掉
                            answer.comments.splice(index,1);

                        } else {
                            alert(r.message);
                        }
                    }
                }
            )
        },
        //采纳问题
        answerSolved:function (answerId){
            if(!answerId){
                alert("不能为空")
                return ;
            }
            $.ajax({
                url:"/v1/answers/"+answerId+"/solved",
                method:"GET",//没有表单哦~
                success:function (r){
                    console.log(r)
                    if(r.code==OK){
                        alert(r.message);
                    }else {
                        alert("不等于OK+"+r.message);
                    }
                }
            })
        }

    },
    created: function () {
        this.loadAnswers();
    }
});