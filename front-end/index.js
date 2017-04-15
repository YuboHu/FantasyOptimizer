var serverURL = 'http://52.25.159.155:8081/FantasyServer';

window.onload = function () {
  $.ajax({
    url: serverURL+'/rank',
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

  $.ajax({
    url: serverURL+'/lineup',
    method: 'GET',
    dataType: 'json'
  }).done(function (data) {
    var i;
    for(i=0; i<data.lineup.length; i++) {
      var title = document.createElement('tr');
      title.className = 'lineupX-title';
      if(i==0) {
        title.className += ' first-row';
      }
      var td = document.createElement('td');
      td.colSpan = '5';
      td.appendChild(document.createTextNode('阵容 ' + (i+1) + ' 预计得分：' + data.lineup[i].total.toFixed(2)));
      title.appendChild(td);
      $('.lineup-table tbody')[0].appendChild(title);

      var players = document.createElement('tr');
      players.className = 'lineupX-players';
      var pg = document.createElement('td');
      pg.id = 'lineup-'+i+'-pg';
      players.appendChild(pg);
      var sg = document.createElement('td');
      sg.id = 'lineup-'+i+'-sg';
      players.appendChild(sg);
      var sf = document.createElement('td');
      sf.id = 'lineup-'+i+'-sf';
      players.appendChild(sf);
      var pf = document.createElement('td');
      pf.id = 'lineup-'+i+'-pf';
      players.appendChild(pf);
      var c = document.createElement('td');
      c.id = 'lineup-'+i+'-c';
      players.appendChild(c);
      $('.lineup-table tbody')[0].appendChild(players);

      $('#lineup-'+i+'-pg').append($('#lineup-player-info-tmp').render([{'position':'PG','name':data.lineup[i].pg.name}]));
      $('#lineup-'+i+'-pg > .player-name').width(window.innerWidth*0.20-10);
      $('#lineup-'+i+'-pg > .img-bg-pair > .player-img')[0].style.backgroundImage = 'url('+data.lineup[i].pg.headImg+')';

      $('#lineup-'+i+'-sg').append($('#lineup-player-info-tmp').render([{'position':'SG','name':data.lineup[i].sg.name}]));
      $('#lineup-'+i+'-sg > .player-name').width(window.innerWidth*0.20-10);
      $('#lineup-'+i+'-sg > .img-bg-pair > .player-img')[0].style.backgroundImage = 'url('+data.lineup[i].sg.headImg+')';

      $('#lineup-'+i+'-sf').append($('#lineup-player-info-tmp').render([{'position':'SF','name':data.lineup[i].sf.name}]));
      $('#lineup-'+i+'-sf > .player-name').width(window.innerWidth*0.20-10);
      $('#lineup-'+i+'-sf > .img-bg-pair > .player-img')[0].style.backgroundImage = 'url('+data.lineup[i].sf.headImg+')';

      $('#lineup-'+i+'-pf').append($('#lineup-player-info-tmp').render([{'position':'PF','name':data.lineup[i].pf.name}]));
      $('#lineup-'+i+'-pf > .player-name').width(window.innerWidth*0.20-10);
      $('#lineup-'+i+'-pf > .img-bg-pair > .player-img')[0].style.backgroundImage = 'url('+data.lineup[i].pf.headImg+')';

      $('#lineup-'+i+'-c').append($('#lineup-player-info-tmp').render([{'position':'C','name':data.lineup[i].c.name}]));
      $('#lineup-'+i+'-c > .player-name').width(window.innerWidth*0.20-10);
      $('#lineup-'+i+'-c > .img-bg-pair > .player-img')[0].style.backgroundImage = 'url('+data.lineup[i].c.headImg+')';
    }
  });

  $.ajax({
    url: serverURL+'/pk',
    method: 'GET',
    dataType: 'json'
  }).done(function (data) {
    var i;
    for(i=0; i<data.pk.length; i++) {
      $('.pk-table > tbody').append($('#pk-tmp').render([{
        'index': i,
        'name0': data.pk[i].player1.name,
        'name1': data.pk[i].player2.name,
        'nameRec': data.pk[i].Recommended,
        'expect': data.pk[i].Certainty.toFixed(2)
      }]));
      $('#pk-'+i+'-img-0')[0].style.backgroundImage = 'url('+data.pk[i].player1.headImg+')';
      $('#pk-'+i+'-name-0').width(window.innerWidth*0.20-10);
      $('#pk-'+i+'-img-1')[0].style.backgroundImage = 'url('+data.pk[i].player2.headImg+')';
      $('#pk-'+i+'-name-1').width(window.innerWidth*0.20-10);
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
    $('.tab-content').hide();
    $('#'+targetId.slice(4)+'-content').show();
  }
}
