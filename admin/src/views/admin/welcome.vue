<template>
  <div>
    <div class="space-12"></div>
    <div class="row">
      <div class="col-xs-12">
        <div class="text-center">
          <span class="btn btn-app btn-sm btn-light no-hover">
              <span class="line-height-1 bigger-170 blue"> 518 </span>
              <br>
              <span class="line-height-1 smaller-90"> course </span>
          </span>
          <span class="btn btn-app btn-sm btn-yellow no-hover">
              <span class="line-height-1 bigger-170"> 6,321 </span>

              <br>
              <span class="line-height-1 smaller-90"> big chapter </span>
          </span>

          <span class="btn btn-app btn-sm btn-pink no-hover">
              <span class="line-height-1 bigger-170"> 4,182 </span>

              <br>
              <span class="line-height-1 smaller-90"> small chapter </span>
          </span>

          <span class="btn btn-app btn-sm btn-success no-hover">
              <span class="line-height-1 bigger-170"> 5.23 </span>

              <br>
              <span class="line-height-1 smaller-90"> member </span>
          </span>

          <span class="btn btn-app btn-sm btn-primary no-hover">
              <span class="line-height-1 bigger-170"> 12.76 </span>

              <br>
              <span class="line-height-1 smaller-90"> enroll</span>
          </span>

          <span class="btn btn-app btn-sm btn-grey no-hover">
              <span class="line-height-1 bigger-170"> 32.96 </span>

              <br>
              <span class="line-height-1 smaller-90">comment </span>
          </span>

        </div>

        <div class="space-12"></div>

        <div class="hr hr2 hr-double"></div>

        <div class="space-12"></div>

      </div>

      <div class="col-sm-12 infobox-container">
        <div class="infobox infobox-green">
          <div class="infobox-icon">
            <i class="ace-icon fa fa-comments"></i>
          </div>

          <div class="infobox-data">
            <span class="infobox-data-number">332</span>
            <div class="infobox-content">new comment</div>
          </div>

          <div class="stat stat-success">8%</div>
        </div>

        <div class="infobox infobox-blue">
          <div class="infobox-icon">
            <i class="ace-icon fa fa-twitter"></i>
          </div>

          <div class="infobox-data">
            <span class="infobox-data-number">58</span>
            <div class="infobox-content">new register member</div>
          </div>

          <div class="badge badge-success">
            +10%
            <i class="ace-icon fa fa-arrow-up"></i>
          </div>
        </div>

        <div class="infobox infobox-pink">
          <div class="infobox-icon">
            <i class="ace-icon fa fa-shopping-cart"></i>
          </div>

          <div class="infobox-data">
            <span class="infobox-data-number">108</span>
            <div class="infobox-content">course sign up</div>
          </div>
          <div class="stat stat-important">4%</div>
        </div>

        <div class="space-12"></div>

        <div class="hr hr2 hr-double"></div>

        <div class="space-12"></div>

      </div>

      <div class="col-sm-12">
        <div class="widget-box transparent">
          <div class="widget-header widget-header-flat">
            <h4 class="widget-title lighter">
              <i class="ace-icon fa fa-signal"></i>
              sales
            </h4>

          </div>

          <div class="widget-body">
            <div class="widget-main padding-4">
              <div id="sales-charts"></div>
            </div><!-- /.widget-main -->
          </div><!-- /.widget-body -->
        </div><!-- /.widget-box -->

        <div class="space-12"></div>

        <div class="hr hr2 hr-double"></div>

        <div class="space-12"></div>
      </div><!-- /.col -->

      <div class="col-sm-6">
        <div class="widget-box transparent">
          <div class="widget-header widget-header-flat">
            <h4 class="widget-title lighter">
              <i class="ace-icon fa fa-star orange"></i>
              course sale order
            </h4>

          </div>

          <div class="widget-body">
            <div class="widget-main no-padding">
              <table class="table table-bordered table-striped">
                <thead class="thin-border-bottom">
                <tr>
                  <th>
                    <i class="ace-icon fa fa-caret-right blue"></i>course name
                  </th>

                  <th>
                    <i class="ace-icon fa fa-caret-right blue"></i>price
                  </th>

                  <th>
                    <i class="ace-icon fa fa-caret-right blue"></i>purchase number
                  </th>
                </tr>
                </thead>

                <tbody>
                <tr>
                  <td>Vim</td>

                  <td>
                    <b class="green">￥299.00</b>
                  </td>

                  <td>
                    <span class="label label-warning arrowed arrowed-right">5,132</span>
                  </td>
                </tr>

                <tr>
                  <td>Vim</td>

                  <td>
                    <b class="red">￥388.00</b>
                  </td>

                  <td>
                    <span class="label label-warning arrowed arrowed-right">3,132</span>
                  </td>
                </tr>

                <tr>
                  <td>Vim</td>
                  <td>
                    <b class="green">￥199.00</b>
                  </td>

                  <td>
                    <span class="label label-warning arrowed arrowed-right">2,132</span>
                  </td>
                </tr>

                <tr>
                  <td>Linux</td>

                  <td>
                    <b class="green">￥299.00</b>
                  </td>

                  <td>
                    <span class="label label-success arrowed arrowed-right">1,654</span>
                  </td>
                </tr>

                <tr>
                  <td>emacs</td>

                  <td>
                    <b class="green">￥199.00</b>
                  </td>

                  <td>
                    <span class="label label-success arrowed arrowed-right">1,254</span>
                  </td>
                </tr>
                </tbody>
              </table>
            </div><!-- /.widget-main -->
          </div><!-- /.widget-body -->
        </div><!-- /.widget-box -->
      </div><!-- /.col -->
      <div class="col-sm-6">
        <div class="widget-box transparent">
          <div class="widget-header widget-header-flat">
            <h4 class="widget-title lighter">
              <i class="ace-icon fa fa-star orange"></i>
              sales order 
            </h4>

          </div>

          <div class="widget-body">
            <div class="widget-main">
              <div id="piechart-placeholder"></div>

            </div><!-- /.widget-main -->
          </div><!-- /.widget-body -->
        </div><!-- /.widget-box -->
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div>
</template>

<script>
  export default {
    name: "welcome",
    mounted: function () {
      let _this = this;
      _this.drawSaleChart();
      _this.drawPieChart();
    },
    methods: {
      drawSaleChart() {
        // 2 random data
        let d1 = [];
        for (let i = 0; i < 30; i += 1) {
          d1.push([i + 1, 2000 + Math.floor((Math.random()*100)+1)]);
        }
        let d2 = [];
        for (let i = 0; i < 30; i += 1) {
          d2.push([i + 1, 1900 + Math.floor((Math.random()*100)+1)]);
        }

        let sales_charts = $('#sales-charts').css({'width':'100%' , 'height':'220px'});
        $.plot("#sales-charts", [
          { label: "last 30 days", data: d1 },
          { label: "last cycle", data: d2 },
        ], {
          hoverable: true,
          shadowSize: 0,
          series: {
            lines: { show: true },
            points: { show: true }
          },
          xaxis: {
            tickLength: 0
          },
          yaxis: {
            tickLength: 0
          },
          grid: {
            backgroundColor: { colors: [ "#fff", "#fff" ] },
            borderWidth: 1,
            borderColor:'#555'
          }
        });
      },

      drawPieChart() {
        let placeholder = $('#piechart-placeholder').css({'width':'90%' , 'min-height':'180px'});
        let data = [
          { label: "Java",  data: 38.7, color: "#68BC31"},
          { label: "Python",  data: 24.5, color: "#2091CF"},
          { label: "Android",  data: 18.6, color: "#DA5430"},
          { label: "others",  data: 10, color: "#FEE074"},
        ];
        $.plot(placeholder, data, {
          series: {
            pie: {
              show: true,
              tilt:0.8,
              highlight: {
                opacity: 0.25
              },
              stroke: {
                color: '#fff',
                width: 2
              },
              startAngle: 2
            }
          },
          legend: {
            show: true,
            position: "ne",
            labelBoxBorderColor: null,
            margin:[-30,15]
          }
          ,
          grid: {
            hoverable: true,
            clickable: true
          }
        })
      },
    }
  }
</script>
