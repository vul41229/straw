let questionApp=new Vue({
    el:"#questionApp",
    data:{
        question:{}
    },
    methods:{
        loadQuestion:function(){
            // question/detail_teacher.html?149
            // 我们要获得url地址栏中?之后的id值
            let qid=location.search;
            // location.search可以获得地址栏中?之后的内容,具体获取规则如下
            // 如果url中没有? qid=""
            // 如果url中有?但是?之后没有任何内容 qid=""
            // 如果url中有?而且?之后有内容(例如149) qid="?149"
            // 既question/detail_teacher.html?149   ->  qid=?149
            if(!qid){
                // !qid就是判断qid不存在,或理解为qid没有值
                alert("请指定问题的id");
                return;
            }
            //  qid   ?149  -> 149
            qid=qid.substring(1);
            axios({
                url:"http://localhost:9000/v2/questions/"+qid,
                method:"get"
            }).then(function(response){
                questionApp.question=response.data;
                addDuration(response.data);
            })
        },
        updateDuration:function(){
            //创建问题时候的时间毫秒数
            let createtime = new Date(this.question.createtime).getTime();
            //当前时间毫秒数
            let now = new Date().getTime();
            let duration = now - createtime;
            if (duration < 1000*60){ //一分钟以内
                this.question.duration = "刚刚";
            }else if(duration < 1000*60*60){ //一小时以内
                this.question.duration =
                    (duration/1000/60).toFixed(0)+"分钟以前";
            }else if (duration < 1000*60*60*24){
                this.question.duration =
                    (duration/1000/60/60).toFixed(0)+"小时以前";
            }else {
                this.question.duration =
                    (duration/1000/60/60/24).toFixed(0)+"天以前";
            }
        }
    },
    created:function(){
        this.loadQuestion()
    }
})
let postAnswerApp=new Vue({
    el:"#postAnswerApp",
    data:{},
    methods: {
        postAnswer:function(){

            let qid=location.search;
            if(!qid){
                alert("qid没有值!!!!");
                return;
            }
            qid=qid.substring(1);

            let content=$("#summernote").val();

            let form=new FormData();
            form.append("questionId",qid);
            form.append("content",content);
            form.append("accessToken",token);
            axios({
                url:"http://localhost:9000/v2/answers",
                method:"post",
                data:form
            }).then(function(response){
                console.log(response.data);
                let answer = response.data;
                answer.duration="剛剛";
                answersApp.answers.push(answer);
                $("#summernote").summernote("reset");
            })

        }
    }
})
let answersApp=new Vue({
    el:"#answersApp",
    data:{
        answers:[]
    },
    methods:{
        loadAnswers:function(){
            // 这个方法也需要问题的id,就在url?之后
            let qid=location.search;
            if(!qid){
                return;
            }
            qid=qid.substring(1);
            axios({
                url:"http://localhost:9000/v2/answers/question/" + qid,
                method:"get"
            }).then(function(response){
                answersApp.answers=response.data;

                answersApp.updateDuration();
            })
        },
        postComment:function(answerId){

            let textarea=$("#addComment"+answerId+" textarea");
            let content=textarea.val();

            let form=new FormData();
            form.append("answerId",answerId);
            form.append("content",content);
            form.append("accessToken",token);
            axios({
                url:"http://localhost:9000/v2/comments",
                method:"post",
                data:form
            }).then(function(result){
                console.log(result.data);

                let comment=result.data;
                // 获得所有回答
                let answers=answersApp.answers;
                // 遍历所有回答
                for(let i=0;i<answers.length;i++){
                    // 判断当前回答的id是否和新增评论的answerId一致
                    if(answers[i].id==answerId){
                        // 如果一致,表示新增的评论对象就是当前回答的
                        // 所以添加到当前回答的评论列表中
                        answers[i].comments.push(comment);
                        // 清空输入评论框中的内容
                        textarea.val("");
                        break;
                    }
                }
            })
        },
        removeComment:function(commentId,index,comments){
            axios({
                url:"http://localhost:9000/v2/comments/"+commentId+"/delete",
                method:"get",
                params:{
                    accessToken:token
                }
            }).then(function(result){

                if(result.data=="ok"){
                    // splice表示要从数组对象中删除元素
                    // 两个参数([删除的起始下标],[删除的个数])
                    alert("刪除成功");
                    comments.splice(index,1);
                }
            })
        },
        updateComment:function(commentId,index,answer){
            //需要获得修改后的评论内容,使用后代选择器获得对象和评论内容
            let textarea=$("#editComment"+commentId+" textarea");
            let content=textarea.val();
            // 如果内容是空就不修改了
            if(!content){
                return;
            }
            // 创建表单,封装CommentVO对象的数据
            // 这里利用SpringValidation验证,所以answerId也要传过去,但是并不使用
            let form=new FormData();
            form.append("answerId",answer.id);
            form.append("content",content);
            form.append("accessToken",token);
            axios({
                url:"http://localhost:9000/v2/comments/"+commentId+"/update",
                data:form,
                method:"post"
            }).then(function(result){
                console.log("使用typeof判斷返回值的類型:" +typeof(result.data));
                if(typeof(result.data)=="object") {
                    let comment = result.data;
                    Vue.set(answer.comments, index, comment);
                    $("#editComment" + commentId).collapse("hide");
                }else{
                    alert(result.data);
                }
            })
        },
        answerSolved:function(answerId){
            axios({
                url:"http://localhost:9000/v2/answers/"+answerId+"/solved",
                method:"get",
                params:{
                    accessToken:token
                }
            }).then(function(result){
                console.log(result.data);
            })
        },
        updateDuration:function() {
            let answers = this.answers;
            for (let i = 0; i < answers.length; i++) {
                addDuration(answers[i]);
            }
        }
    },
    created:function(){
        this.loadAnswers();
    }
})

