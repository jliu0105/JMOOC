<template>
  <main role="main">

    <section class="jumbotron text-center">
      <div class="container">
        <h1>Extra info</h1>
        <p class="lead text-muted m-3">
          control panel：<a href="http://admin.courseimooc.com" target="_blank"><b>admin.courseimooc.com</b></a>，username&password: test/test
        </p>
        <p class="lead text-muted m-3">
          Made with LOVE from Jiyu(Jony) Liu
      </div>
    </section>

    <div class="album py-5 bg-light">
      <div class="container">
        <div class="title1">new courses</div>
        <div class="row">
          <div v-for="o in news" class="col-md-4">
            <the-course v-bind:course="o"></the-course>
          </div>
        </div>

        <hr>

        <div class="title2">course recomandation</div>
        <div class="row">
          <div v-for="o in news" class="col-md-4">
            <the-course v-bind:course="o"></the-course>
          </div>
        </div>
      </div>
    </div>

  </main>
</template>

<script>

  import TheCourse from "../components/the-course";
  export default {
    name: 'index',
    components: {TheCourse},
    data: function () {
      return {
        news: [],
      }
    },
    mounted() {
      let _this = this;
      _this.listNew();
    },
    methods: {
      listNew() {
        let _this = this;

        if (!Tool.isEmpty(news)) {
          _this.news = news;
          return;
        }

        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/web/course/list-new').then((response)=>{
          console.log("recommand result：", response);
          let resp = response.data;
          if (resp.success) {
            _this.news = resp.content;
            // save 
            SessionStorage.set("news", _this.news);
          }
        }).catch((response)=>{
          console.log("error：", response);
        })
      },
    }
  }
</script>

<style>
  .title1{
    margin-bottom: 2rem;
    color: #fafafa;
    letter-spacing: 0;
    text-shadow: 0px 1px 0px #999, 0px 2px 0px #888, 0px 3px 0px #777, 0px 4px 0px #666, 0px 5px 0px #555, 0px 6px 0px #444, 0px 7px 0px #333, 0px 8px 7px #001135;
    font-size: 2rem;
  }
  .title2{
    margin-bottom: 2rem;
    color: transparent;
    -webkit-text-stroke: 1px black;
    letter-spacing: 0.04em;
    font-size: 2rem;
  }
</style>
