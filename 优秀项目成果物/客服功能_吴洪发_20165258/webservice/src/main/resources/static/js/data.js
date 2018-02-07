var jizu_article = [
    {id:1,title:"二进制数据",introduce:"本文主要讲述了二进制数据的表示方式  (点击查看详情)"},
    {id:2,title:"体系结构",introduce:"本文主要讲述了体系结构  (点击查看详情)"},
    {id:3,title:"微体系结构",introduce:"本文主要讲述了微体系结构  (点击查看详情)"},
];
var jiwang_article = [
    {id:1,title:"链路层",introduce:"本文主要讲述了链路层的知识  (点击查看详情)"},
    {id:2,title:"网络层",introduce:"本文主要讲述了网络层 (点击查看详情)"},
    {id:3,title:"运输层",introduce:"本文主要讲述了运输层  (点击查看详情)"},
];
var jiegou_article = [
    {id:1,title:"数组和链表",introduce:"本文主要讲述了数组和链表的编写方式  (点击查看详情)"},
    {id:2,title:"栈和堆",introduce:"本文主要讲述了栈和堆的编写方式  (点击查看详情)"},
    {id:3,title:"二叉树",introduce:"本文主要讲述了二叉树的编写方式  (点击查看详情)"},
];
var jizu = {id:1,name:"计组",articles:jizu_article};
var jiwang = {id:2,name:"计网",articles:jiwang_article};
var jiegou = {id:3,name:"数据结构",articles:jiegou_article};
var groups_data= new Array();
groups_data.push(jizu);
groups_data.push(jiwang);
groups_data.push(jiegou);

messages=[
    {id:1,title:"笔记",content:"你添加一条笔记",action:1},
    {id:2,title:"备忘录",content:"你删除一条备忘录",action:2},
    {id:3,title:"备忘录",content:"你完成了一条笔记",action:3},
];
nodes=[
    {id:1,time:"2017/12/04 04:03",content:"web实验课",finish:false},
    {id:2,time:"2017/12/04 04:03",content:"汇编实验课",finish:false},
    {id:3,time:"2017/12/04 04:03",content:"数据结构实验课",finish:true},
    {id:4,time:"2017/12/04 04:03",content:"计网实验课",finish:false},

];
albums=[
    {id:1,src:"img/1.jpg",content:"这个风景很美"},
    {id:2,src:"img/2.jpg",content:"这个花很美"},
    {id:3,src:"img/3.jpg",content:"这个风景很美"},
    {id:4,src:"img/4.jpg",content:"这个风景很美"},
    {id:5,src:"img/3.jpg",content:"这个风景很美"},
    {id:6,src:"img/2.jpg",content:"这个花很美"},
    {id:7,src:"img/1.jpg",content:"这个风景很美"},
    {id:8,src:"img/4.jpg",content:"这个风景很美"},
];
