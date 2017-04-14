window.onload = function () {
  $.ajax({
    url: 'http://192.168.0.10:8081/FantasyServer/rank',
    method: 'GET',
    dataType: 'json'
  }).done(function (data) {
    var rankData = data.rank;
    var i;
    for(i=0; i<10; i++) {
      $('#pp-table-rank-'+i).width(window.innerWidth*0.1);
      $('#pp-table-pg-'+i).append($('#player-info-tmp').render([{'name':rankData.pg[i].name}]));
      $('#pp-table-pg-'+i+' > .player-name').width(window.innerWidth*0.18-11);
      $('#pp-table-pg-'+i+' > .img-bg-pair > .player-img')[0].style.backgroundImage = 'url('+rankData.pg[i].headImg+')';

      $('#pp-table-sg-'+i).append($('#player-info-tmp').render([{'name':rankData.sg[i].name}]));
      $('#pp-table-sg-'+i+' > .player-name').width(window.innerWidth*0.18-11);
      $('#pp-table-sg-'+i+' > .img-bg-pair > .player-img')[0].style.backgroundImage = 'url('+rankData.sg[i].headImg+')';

      $('#pp-table-sf-'+i).append($('#player-info-tmp').render([{'name':rankData.sf[i].name}]));
      $('#pp-table-sf-'+i+' > .player-name').width(window.innerWidth*0.18-11);
      $('#pp-table-sf-'+i+' > .img-bg-pair > .player-img')[0].style.backgroundImage = 'url('+rankData.sf[i].headImg+')';

      $('#pp-table-pf-'+i).append($('#player-info-tmp').render([{'name':rankData.pf[i].name}]));
      $('#pp-table-pf-'+i+' > .player-name').width(window.innerWidth*0.18-11);
      $('#pp-table-pf-'+i+' > .img-bg-pair > .player-img')[0].style.backgroundImage = 'url('+rankData.pf[i].headImg+')';

      $('#pp-table-c-'+i).append($('#player-info-tmp').render([{'name':rankData.c[i].name}]));
      $('#pp-table-c-'+i+' > .player-name').width(window.innerWidth*0.18-11);
      $('#pp-table-c-'+i+' > .img-bg-pair > .player-img')[0].style.backgroundImage = 'url('+rankData.c[i].headImg+')';
    }
  });
}

function switchTab(event) {
  var targetId = event.target.id;
  if($('#' + targetId).hasClass('active')) {
    // do nothing
  } else {
    $('li.active').removeClass('active');
    $('#' + targetId).addClass('active');
  }
}
