<template>
  <main role="main">
    <div class="album py-5 bg-light">
      <div class="container">
        <div class="row course-head">
          <div class="col-sm-6" id="cover-video-div">
            <img class="img-fluid" v-bind:src="course.image">
          </div>
          <div class="col-sm-6">
            <h1>{{course.name}}</h1>
            <p class="course-head-item">
              <span><i class="fa fa-clock-o"></i> {{(course.time) | formatSecond}}</span>
              <span>{{COURSE_LEVEL | optionKV(course.level)}}</span>
              <span><i class="fa fa-user"></i> {{course.enroll}}</span>
            </p>
            <p class="course-head-desc">{{course.summary}}</p>
            <p class="course-head-price">
              <span class="price-now text-danger"><i class="fa fa-yen"></i>&nbsp;{{course.price}}&nbsp;&nbsp;</span>
            </p>
            <p class="course-head-button-links">
              <a v-show="!memberCourse.id" v-on:click="enroll()" class="btn btn-lg btn-primary btn-shadow" href="javascript:;">enroll now</a>
              <a v-show="memberCourse.id" href="#" class="btn btn-lg btn-success btn-shadow disabled">you have enrolled</a>
            </p>
          </div>
        </div>

        <div class="row">

          <div class="col-md-9">
            <!-- Nav tabs -->
            <ul class="nav nav-tabs">
              <li class="nav-item">
                <a class="nav-link active" href="#info" data-toggle="tab">course description</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#chapter" data-toggle="tab">Chapter Directory</a>
              </li>
            </ul>

            <br>

            <!-- Tab panes -->
            <div class="tab-content">
              <div class="tab-pane active" id="info" v-html="course.content">
              </div>
              <div class="tab-pane" id="chapter">
                <div v-for="(chapter, i) in chapters" class="chapter">
                  <div v-on:click="doFolded(chapter, i)" class="chapter-chapter">
                    <span>{{chapter.name}}</span>
                    <span class="pull-right">
                      <i v-show="chapter.folded" class="fa fa-plus-square" aria-hidden="true"></i>
                      <i v-show="!chapter.folded" class="fa fa-minus-square" aria-hidden="true"></i>
                    </span>
                  </div>
                  <div v-show="!chapter.folded">
                    <table class="table table-striped">
                      <tr v-for="(s, j) in chapter.sections" class="chapter-section-tr">
                        <td class="col-sm-8 col-xs-12">
                          <div v-on:click="play(s)" class="section-title">
                            <i class="fa fa-video-camera d-none d-sm-inline"></i>&nbsp;&nbsp;
                            <span class="d-none d-sm-inline">number{{j+1}}&nbsp;&nbsp;</span>
                            {{s.title}}
                            <span v-show="s.charge !== SECTION_CHARGE.CHARGE.key" class="badge badge-primary hidden-xs">free_fee</span>
                          </div>
                        </td>
                        <td class="col-sm-1 text-right">
                          <span class="badge badge-primary">{{s.time | formatSecond}}</span>
                        </td>
                      </tr>
                    </table>
                  </div>
                </div>
              </div>
            </div>

          </div>

          <!-- lecturer detail -->
          <div class="col-md-3">
            <div class="card" style="width: 18rem;">
              <img v-bind:src="teacher.image" class="card-img-top">
              <div class="card-body">
                <h5 class="card-title">{{teacher.name}}</h5>
                <p class="card-text">{{teacher.motto}}</p>
                <p class="card-text">{{teacher.intro}}</p>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>

    <modal-player ref="modalPlayer"></modal-player>
  </main>
</template>

<script>

  import ModalPlayer from "../components/modal-player";
  export default {
    name: 'detail',
    components: {ModalPlayer},
    data: function () {
      return {
        id: "",
        course: {},
        teacher: {},
        chapters: [],
        sections: [],
        memberCourse: {},
        COURSE_LEVEL: COURSE_LEVEL,
        SECTION_CHARGE: SECTION_CHARGE
      }
    },
    mounted() {
      let _this = this;
      _this.id = _this.$route.query.id;
      _this.findCourse();
    },
    methods: {
      findCourse() {
        let _this = this;
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/web/course/find/' + _this.id).then((response)=>{
          let resp = response.data;
          _this.course = resp.content;
          _this.teacher = _this.course.teacher || {};
          _this.chapters = _this.course.chapters || [];
          _this.sections = _this.course.sections || [];

          _this.getEnroll();

          // Put all sections into corresponding chapters
          for (let i = 0; i < _this.chapters.length; i++) {
            let c = _this.chapters[i];
            c.sections = [];
            for (let j = 0; j < _this.sections.length; j++) {
              let s = _this.sections[j];
              if (c.id === s.chapterId) {
                c.sections.push(s);
              }
            }

            Tool.sortAsc(c.sections, "sort");
          }
        })
      },

      /**
       * @param chapter
       */
      doFolded (chapter, i) {
        let _this = this;
        chapter.folded = !chapter.folded;
        _this.$set(_this.chapters, i, chapter);
      },

      /**
       * play video
       * @param section
       */
      play(section) {
        let _this = this;
        if (section.charge === _this.SECTION_CHARGE.CHARGE.key ) {
          let loginMember = Tool.getLoginMember();
          if (Tool.isEmpty(loginMember)) {
            _this.$event.$emit("openLoginModal", "111");
            return;
          } else {
            if (Tool.isEmpty(_this.memberCourse)) {
              Toast.warning("please enroll first");
              return;
            }
          }
        }
        _this.$refs.modalPlayer.playVod(section.vod);
      },

      enroll() {
        let _this = this;
        let loginMember = Tool.getLoginMember();
        if (Tool.isEmpty(loginMember)) {
          Toast.warning("please enroll first");
          return;
        }
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/member-course/enroll', {
          courseId: _this.course.id,
          memberId: loginMember.id
        }).then((response)=>{
          let resp = response.data;
          if (resp.success) {
            _this.memberCourse = resp.content;
            Toast.success("enroll success！");
          } else {
            Toast.warning(resp.message);
          }
        });
      },

      getEnroll() {
        let _this = this;
        let loginMember = Tool.getLoginMember();
        if (Tool.isEmpty(loginMember)) {
          console.log("not log in");
          return;
        }
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/member-course/get-enroll', {
          courseId: _this.course.id,
          memberId: loginMember.id
        }).then((response)=>{
          let resp = response.data;
          if (resp.success) {
            _this.memberCourse = resp.content || {};
          }
        });
      },
    }
  }
</script>

<style>
  /* course info */
  .course-head {
  }
  .course-head h1 {
    font-size: 2rem;
    margin-bottom: 1.5rem;
  }
  .course-head-item span {
    margin-right: 1rem;
  }
  .course-head-desc {
    font-size: 1rem;
    color: #555
  }
  .course-head a {
  }
  .course-head-price {
    font-size: 2rem;
  }
  @media (max-width: 700px) {
    .course-head h1 {
      font-size: 1.5rem;
    }
  }

  .chapter {
    padding-bottom: 1.25rem;
  }
  .chapter-chapter {
    font-size: 1.25rem;
    padding: 1.25rem;
    background-color: #23527c;
    color: white;
    cursor: pointer;
  }
  .chapter-section-tr {
    font-size: 1rem;
  }
  .chapter-section-tr td{
    padding: 1rem 1.25rem;
    vertical-align: middle;
  }
  /*mouse gesture*/
  .chapter-section-tr td .section-title{
    color: #555;
  }
  .chapter-section-tr td .section-title:hover{
    color: #23527c;
    font-weight: bolder;
    cursor: pointer;
  }
  .chapter-section-tr td .section-title i{
    color: #2a6496;
  }
  @media (max-width: 700px) {
    .chapter-chapter {
      font-size: 1.2rem;
    }
    .chapter-section-tr {
      font-size: 0.9rem;
    }
  }
</style>
