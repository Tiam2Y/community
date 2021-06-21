/**
 * 向问题提交回复
 */
function post() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

/**
 * 向评论提交回复
 * @param e
 */
function comment(e) {
    //获取 comment 的ID
    let commentId = e.getAttribute("data-id");
    //获取相应的<input></input>标签中的输入内容
    let content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}


function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容!");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",    //需要明确从根目录下寻址
        contentType: 'application/json',
        //将 JS 对象转换为字符串
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code === 200) {
                //重新加载页面
                window.location.reload();
            } else {
                if (response.code === 2003) {
                    let isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=ad3aafde0ac9783ef322&redirect_uri=" + document.location.origin + "/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}


/**
 * 展开二级评论
 * @param e
 */
function collapseComments(e) {
    let id = e.getAttribute("data-id");
    let subCommentContainer = $("#comment-" + id);

    // 如果二级评论中只有一个子元素 -- 那个新增评论的输入框
    // 则获取JSON并渲染二级评论页面，否则不渲染
    if (subCommentContainer.children().length === 1) {
        $.getJSON("/comment/" + id, function (data) {
            $.each(data.data.reverse(), function (index, comment) {
                let mediaLeftElement = $("<div/>", {
                    "class": "media-left"
                }).append($("<img/>", {
                    "class": "media-object img-rounded",
                    "src": comment.user.avatarUrl
                }));

                let mediaBodyElement = $("<div/>", {
                    "class": "media-body"
                }).append($("<h5/>", {
                    "class": "media-heading",
                    "html": comment.user.name
                })).append($("<div/>", {
                    "html": comment.content
                })).append($("<div/>", {
                    "class": "menu"
                }).append($("<span/>", {
                    "class": "pull-right",
                    "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                })));

                let mediaElement = $("<div/>", {
                    "class": "media"
                }).append(mediaLeftElement).append(mediaBodyElement);

                let commentElement = $("<div/>", {
                    "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                }).append(mediaElement);

                subCommentContainer.prepend(commentElement);
            });
        });
    }
    subCommentContainer.toggleClass("in");  //切换二级评论的开与合(渲染好页面之后再开合)
    if (subCommentContainer.hasClass("in")) {
        //打开状态
        e.classList.add("active");
    } else {
        //关闭状态
        e.classList.remove("active");
    }
}

/**
 * 展示折叠的标签页
 */
function showSelectTag() {
    $("#select-tag").show();
}

/**
 * 将选中的标签添加
 * @param e
 */
function selectTag(e) {
    let value = e.getAttribute("data-tag");
    let previous = $("#tag").val();

    if (previous) {
        let index = 0;
        let appear = false; //记录value是否已经作为一个独立的标签出现过
        while (true) {
            index = previous.indexOf(value, index); //value字符串在previous中出现的位置
            if (index === -1) break;
            //判断previous中出现的value是否是另一个标签的一部分
            //即value的前一个和后一个字符都是逗号","或者没有字符时，才说明value是一个独立的标签
            if ((index === 0 || previous.charAt(index - 1) === ",")
                && (index + value.length === previous.length || previous.charAt(index + value.length) === ",")
            ) {
                appear = true;
                break;
            }
            index++; //用于搜索下一个出现位置
        }
        if (!appear) {
            //若value没有作为一个独立的标签出现过
            $("#tag").val(previous + ',' + value);
        }
    } else {
        $("#tag").val(value);
    }
}