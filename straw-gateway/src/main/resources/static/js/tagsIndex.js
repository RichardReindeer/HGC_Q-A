let tags = new Vue({
    el: '#tagsApp',
    data: {
        tags:[],
        questions: [],
        pageInfo: {},
    },
    methods: {
        loadTags:function () {
            console.log('执行了 tagsIndex的 loadTags');
            $.ajax({
                url:'/faq/v1/tags',
                method:'GET',
                success:function (r) {
                    console.log(r);
                    if (r.code === OK){
                        console.log('成功获取tags');
                        //从空数组变为了拥有所有标签的数组
                        tags.tags = r.data;
                    }
                }
            });
        },
        loadQuestionWithTags: function (tagsNum, pageNum) {
            if (!pageNum) {
                pageNum = 1;
            }
            if (!tagsNum) {
                tagsNum = -1;
                console.log("tags Num = -1，没有收到数据")
            }
            console.log("tags是多少啊  ->"+tagsNum)
            $.ajax({
                url: '/faq/v1/tags/tagsQuestion',
                method: "GET",
                data: {
                    tagsNum: tagsNum,
                    pageNum: pageNum
                },
                success: function (r) {
                    console.log("成功加载数据");
                    console.log(r);
                    if (r.code === OK) {
                        questionsApp.questions = r.data.list;
                        questionsApp.pageInfo = r.data;
                        //为question对象添加持续时间属性
                        questionsApp.updateDuration();
                        questionsApp.updateTagImage();
                    }
                }
            });
        }
    },
    created:function () {
        console.log("执行了方法");
        this.loadTags();
    }
});


/*
显示当前用户的问题
 */
let questionsApp = new Vue({
    el:'#questionsApp',
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
                url: '/faq/v1/questions/my',
                method: "GET",
                data:{
                    pageNum:pageNum
                },
                success: function (r) {
                    console.log("成功加载数据");
                    console.log(r);
                    if(r.code === OK){
                        questionsApp.questions = r.data.list;
                        questionsApp.pageInfo = r.data;
                        //为question对象添加持续时间属性
                        questionsApp.updateDuration();
                        questionsApp.updateTagImage();
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
        },
        loadQuestionWithTags:function (tagsNum,pageNum){
            console.log("tagsNum 原来是-->"+tagsNum)
            if(tags.id){
                tagsNum = tags.id
            }
            if(! pageNum){
                pageNum = 1;
            }
            if(!tagsNum){
                tagsNum = -1;
            }
            $.ajax({
                url: '/faq/v1/tags/tagsQuestion',
                method: "GET",
                data:{
                    tagsNum :tagsNum,
                    pageNum:pageNum
                },
                success: function (r) {
                    console.log("成功加载数据");
                    console.log(r);
                    if(r.code === OK){
                        questionsApp.questions = r.data.list;
                        questionsApp.pageInfo = r.data;
                        //为question对象添加持续时间属性
                        questionsApp.updateDuration();
                        questionsApp.updateTagImage();
                    }
                }
            });
        }
    },
    created:function () {
        console.log("执行了方法");
        this.loadQuestionWithTags(-1,1);//默认查第一页
    }
});








