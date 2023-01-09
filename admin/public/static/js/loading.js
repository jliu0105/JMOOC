Loading = {
  show: function () {
    $.blockUI({
      message: '<img src="/static/image/loading.gif" />',
      css: {
        zIndex: "10011",
        padding: "10px",
        left: "50%",
        width: "80px",
        marginLeft: "-40px",
      }
    });
  },
  hide: function () {
    //The local query speed is too fast, and the loading display is displayed for a moment, so it is deliberately delayed
    setTimeout(function () {
      $.unblockUI();
    }, 50)
  }
};
