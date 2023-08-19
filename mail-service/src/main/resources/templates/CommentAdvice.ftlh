<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>新评论通知</title>
</head>
<body>
<div class="container">
    <div class="header">
        <div class="blog-name">
            MashiroBlog
        </div>
    </div>
    <div class="main">
        <div class="title">
            ${title}
        </div>
        <hr/>
        <div class="content">
            <div class="info">
                <div class="sender">
                    @${senderNickname}
                </div>
                <div class="repeat">
                    回复：
                </div>
            </div>
            <div class="comment-content">
                ${content}
            </div>
        </div>
    </div>
    <div class="footer">

    </div>
</div>
</body>
<style>
.container {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.header {
    width: 100%;
    height: 50px;
    line-height: 50px;
    background-color: #393d49;
}

.header .blog-name {
    margin-left: 15px;
    color: #eeeeee;
    font-size: 30px;
    font-weight: 700;
}

.main {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 25px;
    padding: 25px 50px 50px;
    width: 600px;
    height: auto;
    text-align: center;
    box-sizing: border-box;
}

.main hr {
    width: 400px;
}

.main .title {
    font-size: 26px;
}

.main .content {
    padding: 15px;
    flex-direction: column;
    font-size: 20px;
    width: 500px;
    background-color: #EEEFEF;
}

.main .content .info {
    display: flex;
    flex-direction: row;
}

.main .content .info .sender {
    color: #666FFF;
}

.main .content .info .repeat {
    margin-left: 10px;
}

.main .content .comment-content {
    display: flex;
    margin-top: 10px;
    font-size: 18px;
}
</style>
</html>