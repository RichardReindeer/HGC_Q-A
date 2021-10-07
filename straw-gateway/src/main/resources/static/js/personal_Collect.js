/**
 * 用户收藏问题页面
 */


let collectQuestions = new Vue({
    el:'#collectQuestionsApp',
    data: {
        questions:[],
        pageInfo:{},
    },
    methods: {
        loadQuestions:function (pageNum) {
            if(! pageNum){
                pageNum = 1;
            }
            $.ajax({
                url: '/faq/v1/questions/myCollect',
                method: "GET",
                data:{
                    pageNum:pageNum
                },
                success: function (r) {
                    console.log("collectQuestions 成功加载数据");
                    console.log(r);
                    if(r.code === 'OK'){
                        collectQuestions.questions = r.data.list;
                        collectQuestions.pageInfo = r.data;
                        //为question对象添加持续时间属性
                        collectQuestions.updateDuration();
                        collectQuestions.updateTagImage();
                    }
                }
            });
        },
        updateTagImage:function(){
            let questions = this.questions;
            for(let i=0; i<questions.length; i++){
                let tags = questions[i].tags;
                //js中if([变量]) 这种写法
                // 就是在判断这个变量存在与否，如果存在则返回true
                if(tags){

                    let tagImage = '/img/tags/'+tags[0].id+'.jpg';
                    console.log(tagImage);
                    questions[i].tagImage = tagImage;
                }
            }
        },
        updateDuration:function () {
            let questions = this.questions;
            // 遍历所有问题
            for(let i=0; i<questions.length; i++){
                addDuration(questions[i]);
            }
        }
    },
    created:function () {
        console.log("执行了方法");
        this.loadQuestions(1);//默认查第一页
    }
});