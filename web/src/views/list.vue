<template>
  <main role="main">
    <div class="header-nav">
      <div class="clearfix">
        <div class="container">
          <div class="row">
            <div class="col-12">
              <a v-on:click="onClickLevel1('00000000')" id="category-00000000" href="javascript:;" class="cur">all</a>
              <a v-for="o in level1" v-on:click="onClickLevel1(o.id)" v-bind:id="'category-' + o.id" href="javascript:;">{{o.name}}</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="skill clearfix">
      <div class="container">
        <div class="row">
          <div class="col-12">
            <a v-on:click="onClickLevel2('11111111')" id="category-11111111" href="javascript:;" class="on">unlimited</a>
            <a v-for="o in level2" v-on:click="onClickLevel2(o.id)" v-bind:id="'category-' + o.id" href="javascript:;">{{o.name}}</a>

            <div style="clear:both"></div>
          </div>
        </div>
      </div>
    </div>
    <div class="album py-5 bg-light">
      <div class="container">
        <div class="row">
          <div class="col-md-12">
            <pagination ref="pagination" v-bind:list="listCourse"></pagination>
          </div>
        </div>
        <br>
        <div class="row">
          <div v-for="o in courses" class="col-md-4">
            <the-course v-bind:course="o"></the-course>
          </div>
          <h3 v-show="courses.length === 0">course has not on yet</h3>
        </div>
      </div>
    </div>

  </main>
</template>

<script>
  import TheCourse from "../components/the-course";
  import Pagination from "../components/pagination";

  export default {
    components: {Pagination, TheCourse},
    name: 'list',
    data: function () {
      return {
        courses: [],
        level1: [],
        level2: [],
        categorys: [],
        level1Id: "",
        level2Id: "",
      }
    },
    mounted() {
      let _this = this;
      _this.$refs.pagination.size = 3;
      _this.listCourse(1);
      _this.allCategory();
    },
    methods: {
      listCourse(page) {
        let _this = this;
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/course/list', {
          page: page,
          size: _this.$refs.pagination.size,
          categoryId: _this.level2Id || _this.level1Id || "", 
        }).then((response) => {
          let resp = response.data;
          if (resp.success) {
            _this.courses = resp.content.list;
            _this.$refs.pagination.render(page, resp.content.total);
          }
        }).catch((response) => {
          console.log("error：", response);
        })
      },

      allCategory() {
        let _this = this;
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/category/all').then((response)=>{
          let resp = response.data;
          let categorys = resp.content;
          _this.categorys = categorys;

          // Format all records into a tree structure
          _this.level1 = [];
          for (let i = 0; i < categorys.length; i++) {
            let c = categorys[i];
            if (c.parent === '00000000') {
              _this.level1.push(c);
            } else {
              _this.level2.push(c);
            }
          }
        })
      },

      /**
       * @param level1Id
       */
      onClickLevel1(level1Id) {
        let _this = this;

        // When clicking on the first-level classification, set variables for course filtering, the second-level classification id is empty, if you click [All], the first-level classification id is empty
        _this.level2Id = null;
        _this.level1Id = level1Id;
        if (level1Id === "00000000") {
          _this.level1Id = null;
        }

        // When clicking on the first-level category, the activation status is displayed
        $("#category-" + level1Id).siblings("a").removeClass("cur");
        $("#category-" + level1Id).addClass("cur");

        // When clicking the first-level classification, the second-level classification [Unlimited] button should be set to the active state
        $("#category-11111111").siblings("a").removeClass("on");
        $("#category-11111111").addClass("on");

        // Note: You must first clear all the values in level2, and then put them in
        _this.level2 = [];
        let categorys = _this.categorys;
        // If [All] is clicked, all secondary categories will be displayed
        if (level1Id === '00000000') {
          for (let i = 0; i < categorys.length; i++) {
            let c = categorys[i];
            if (c.parent !== "00000000") {
              _this.level2.push(c);
            }
          }
        }
        // If you click on a first-level classification, the second-level classification under the first-level classification will be displayed
        if (level1Id !== '00000000') {
          for (let i = 0; i < categorys.length; i++) {
            let c = categorys[i];
            if (c.parent === level1Id) {
              _this.level2.push(c);
            }
          }
        }

        //Reload the course list 
        _this.listCourse(1);
      },

      /**
       * @param level1Id
       */
      onClickLevel2(level2Id) {
        let _this = this;
        $("#category-" + level2Id).siblings("a").removeClass("on");
        $("#category-" + level2Id).addClass("on");

        if (level2Id === "11111111") {
          _this.level2Id = null;
        } else {
          _this.level2Id = level2Id;
        }

        // reload course
        _this.listCourse(1);
      },

    }
  }
</script>
<style>
  .header-nav {
    height: auto;
    background: #fff;
    box-shadow: 0 8px 16px 0 rgba(28,31,33,.1);
    padding: 16px 0;
    box-sizing: border-box;
    position: relative;
    z-index: 1;
    /*background-color: #d6e9c6;*/
  }
  .header-nav>div {
    width: 100%;
    padding-left: 12px;
    box-sizing: border-box;
    margin-left: auto;
    margin-right: auto;
    /*background-color: #B4D5AC;*/
  }
  .header-nav a {
    float: left;
    font-size: 16px;
    color: #07111b;
    line-height: 50px;
    height: 45px;
    position: relative;
    margin-right: 46px;
    font-weight: 700;
  }
  .header-nav a:hover {
    color: #c80;
  }
  .header-nav a.cur {
    color: #c80;
  }
  .header-nav a.cur:before {
    display: block;
  }
  .header-nav a:before {
    display: none;
    content: ' ';
    position: absolute;
    bottom: 0;
    background: #c80;
    width: 16px;
    height: 3px;
    left: 50%;
    margin-left: -8px;
  }
  .skill {
    width: 100%;
    padding: 24px 0 0;
    position: relative;
    margin: 0 auto;
  }
  .skill a.on {
    color: #c80;
    background: rgba(204,136,0,.1);
  }
  .skill a {
    float: left;
    margin-right: 20px;
    padding: 0 12px;
    font-size: 14px;
    color: #4d555d;
    line-height: 32px;
    border-radius: 6px;
    margin-bottom: 12px;
  }
  .skill a:hover {
    background: #d9dde1;
  }
</style>
