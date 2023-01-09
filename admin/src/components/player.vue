<template>
  <div v-bind:id="playerId">
<!--    <div  class="prism-player" id="J_prismPlayer"></div>-->
  </div>
</template>
<script>
  export default {
    name: 'player',
    props: {
      playerId: {
        default: "player-div"
      },
    },
    data: function () {
      return {
        aliPlayer: {}, 
      }
    },
    methods: {
      playUrl(url) {
        let _this = this;
        console.log("start playing:", url);

        if (_this.aliPlayer) {
          _this.aliPlayer = null;
          $("#" + _this.playerId + '-player').remove();
        }

        // start player
        $("#" + _this.playerId).append("<div class=\"prism-player\" id=\"" + _this.playerId + "-player\"></div>");
        _this.aliPlayer = new Aliplayer({
          id: _this.playerId + '-player',
          width: '100%',
          autoplay: false,
          source: url,
          cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png',
        }, function (player) {
          console.log('start player success.')
        });
      },

      playVod (vod) {
        let _this = this;
        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/file/admin/get-auth/' + vod).then((response)=>{
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            // If there is already a player, delete the player div
            if (_this.aliPlayer) {
              _this.aliPlayer = null;
              $("#" + _this.playerId + '-player').remove();
            }

            $("#" + _this.playerId).append("<div class=\"prism-player\" id=\"" + _this.playerId + "-player\"></div>");
            _this.aliPlayer = new Aliplayer({
              id: _this.playerId + '-player',
              width: '100%',
              autoplay: false,
              vid: vod,
              playauth: resp.content,
              cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png',
              encryptType:1, 
            },function(player){
              console.log('start player success.')
            });
          } else {
            Toast.warning('playing error')
          }
        })

      }
    }
  }
</script>