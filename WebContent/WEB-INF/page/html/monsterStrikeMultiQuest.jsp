<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>モンストマルチクエスト情報</title>
<!--[if lt IE 9]>
<script type="text/javascript" src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jquerymobile/1.4.5/jquery.mobile.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquerymobile/1.4.5/jquery.mobile.min.js"></script>

<!--
  独自javascript
-->
<script type="text/javascript">
$(function(){0
/* $(document).on("pagecreate", function(event, ui) { */

  // 前回出力時のマルチクエスト情報
  var oldMultiQuestList;

  /**
   * 初期処理
   */
  {
    // ストレージから値の取得
    f_get_storage();

    // データ取得処理
    f_get_data();
  }

  /**
   * 更新ボタン押下処理
   */
  $("#refreshAnchor").on('click', function(){
    // データ取得処理
    f_get_data();
  });

  /**
   * データ取得処理
   */
  function f_get_data(bbsType){
    // くるくる表示
    $.mobile.loading('show');
    // リフレッシュアンカー非表示
    $("#refreshAnchor").hide();

    var url = location.pathname + "main";
    $.ajax({
      url: url,
      type: "POST",
      data: {
        "bbsType" : $("#bbsType").val(),  //bbsType
      },
      dataType: 'json',
      timeout: 5000, // 5秒
    }).done(function(data, textStatus, jqXHR){
      // 成功

      // ストレージに値を格納
      f_set_storage();

      // データ反映処理
      f_data_reflect(data);

      // データ自動選択処理
      f_data_auto_select();

      // データ自動更新処理
      f_data_auto_refresh();

      // 画面の再描画処理
      f_display_refresh();

    }).fail(function(data, textStatus, errorThrown){
      // 失敗
      alert("Error");
    }).always(function(data, textStatus, returnedObject){
      // くるくる非表示
      $.mobile.loading('hide');
      // リフレッシュアンカー表示
      $("#refreshAnchor").show();
    });
  }

  /**
   * データ反映処理
   * @param {json} data
   */
  function f_data_reflect(data){

    // クエストエリアのクリア
    $("#questArea *").remove();

    // マルチクエスト情報
    var multiQuestList = data;

    // 反映処理
    var cnt=0;
    for(var i=0; i < multiQuestList.length; i++){
      var multiQuest = multiQuestList[i];

      // フィルタチェック
      var hasFilter = f_data_filter(multiQuest);
      if(!hasFilter){
        // フィルタ対象外の場合は非表示
        continue;
      }

      // タグ要素の生成
      var recodeTag = $("<tr>");
      // IDタグ
      var idTag = $("<td>").text(multiQuest.id.substr(-3,3)); // 末尾３文字
      // タイトル、URLタグ
      var questTag = $("<td>");
      var questUrlTage = $("<a>").text(multiQuest.questTitle)
                                 .attr("href", multiQuest.questUrl);
      // メッセージタグ
      var questMsgTag = $("<span>").text("　→　" + multiQuest.questMsg);

      // タグ要素の結合
      questTag.append(questUrlTage);
      if(multiQuest.questMsg != ""){
          questTag.append($("<br>"));
          questTag.append(questMsgTag);
      }
      recodeTag.append(idTag);
      recodeTag.append(questTag);

      // タグ要素をクエストエリアに出力
      $("#questArea").append(recodeTag);
      cnt++;

      // 件数制限
      if(cnt >= 3){
        break;
      }
    }

    // 反映データの記憶
    oldMultiQuestList = multiQuestList;
  }

  /**
   * フィルタ処理
   * @param multiQuest
   * @return 結果[true:表示  false:非表示]
   */
  function f_data_filter(multiQuest){

    /* 時間フィルタ */
    if(multiQuest.time != "数秒前"){
      // 非表示
      return false;
    }

    /* タイトルフィルタ */
    var filterQuestTitle = $("#filterQuestTitle").val().trim();
    if(filterQuestTitle != ""){
      var regxVal = ".*"+ filterQuestTitle.replace( / /g , ".*" ) +".*"; // 部分一致＋複数一致
      if(new RegExp(regxVal).test(multiQuest.questTitle) == false){
        // 非表示
        return false;
      }
    }

    /* メッセージフィルタ */
    var filterQuestMsg = $("#filterQuestMsg").val().trim();
    if(filterQuestMsg != ""){
      var regxVal = ".*"+ filterQuestMsg.replace( / /g , ".*" ) +".*"; // 部分一致＋複数一致
      if(new RegExp(regxVal).test(multiQuest.questMsg) == false){
        // 非表示
        return false;
      }
    }

    /* 前回表示データフィルタ */
    if(oldMultiQuestList != null){
      for(var i=0; i < oldMultiQuestList.length; i++){
        var oldMultiQuest = oldMultiQuestList[i];
        if(multiQuest.id == oldMultiQuest.id){
          // 非表示
          return false;
        }
      }
    }

    // 表示
    return true;
  }


  /**
   * データ自動選択処理
   */
  function f_data_auto_select(){
    if(!$("#autoSelect").prop('checked')){
      // チェックOFFの場合は終了
      return;
    }

    var firstUrlTag = $("#questArea a:first");
    if(firstUrlTag.length > 0){
      // 要素が存在する場合は遷移
      location.href = firstUrlTag.attr("href");
    }
  }

  /**
   * データ自動更新処理
   */
  function f_data_auto_refresh(){
    if(!$("#autoRefresh").prop('checked')){
      // チェックOFFの場合は終了
      return;
    }

    // 1秒後に自動更新
    var ms = 1 * 1000;
    setTimeout(function(){
            f_get_data();
        }, ms);
  }

  /**
   * ストレージから値の取得
   */
  function f_get_storage(){
    // ローカルストレージ
    $("#bbsType").val(localStorage.getItem("bbsType"));
    $("#filterQuestTitle").val(localStorage.getItem("filterQuestTitle"));
    $("#filterQuestMsg").val(localStorage.getItem("filterQuestMsg"));
    $("#autoRefresh").prop("checked", localStorage.getItem("autoRefresh"));
    // セッションストレージ
    $("#autoSelect").prop("checked", sessionStorage.getItem("autoSelect"));

  }

  /**
   * ストレージに値の格納
   */
  function f_set_storage(){
    // ローカルストレージ
    localStorage.setItem("bbsType", $("#bbsType").val());
    localStorage.setItem("filterQuestTitle", $("#filterQuestTitle").val());
    localStorage.setItem("filterQuestMsg", $("#filterQuestMsg").val());
    localStorage.setItem("autoRefresh", $("#autoRefresh").prop("checked"));
    // セッションストレージ
    sessionStorage.setItem("autoSelect", $("#autoSelect").prop("checked"));
  }

  /**
   * 画面の再描画処理
   */
  function f_display_refresh(){
    $("input[type='checkbox']").checkboxradio("refresh");
    $("input[type='radio']").checkboxradio("refresh");
    $("select").selectmenu("refresh");
  }

});
</script>


<!--
  独自css
-->
<style type="text/css">

/**
 * 全体構成
 */
body {
  font-family: "メイリオ","Hiragino Kaku Gothic ProN",sans-serif;
  font-size: 90%;
}

article,
footer {
  font-size: 90%;
}

.ui-header .ui-title {
  margin-left: 15%;
  margin-right: 15%
}

</style>

</head>
<body>
<form name="formMain" method="post" autocomplete="on">


<div id="page" data-role="page" data-theme="b">

  <!--
    ヘッダ
  -->
  <header data-role="header" data-theme="b" data-position="fixed">
    <h1>モンストマルチクエスト情報</h1>
    <a href="#" id="refreshAnchor" class="ui-btn ui-btn-right ui-btn-icon-notext ui-icon-refresh"></a>
    <hr>
  </header>

  <!--
    コンテンツ
  -->
  <article data-role="content" data-theme="b">


    <!--
      マルチクエスト情報
    -->
    <div class="multiQuestSection">
      <h3>マルチクエスト</h3>
      <div style="height: 12em">
        <table>
          <colgroup>
            <col style="width: 3em">
            <col style="width: 25em">
          </colgroup>
          <thead>
            <tr>
              <th align="left">ID</th>
              <th align="left">クエスト</th>
            </tr>
          </thead>
          <tbody id="questArea">
            <tr>
              <td>X12</td>
              <td>
                <a href="#">XXXXXXXXXXXXXX（究極）</a><br>
                XXXXXXXのみ
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    <br>
    <hr>
    </div>

    <!--
      掲示板情報
    -->
    <div class="bbsTypeSection">
      <h3>掲示板設定</h3>
      <select id="bbsType" data-mini="true">
        <option value="3" >運極1[降臨/イベント]</option>
        <option value="1" >運極2[降臨/イベント]</option>
        <option value="33">運極[タス/亀/曜日]</option>
        <option value="4" >攻略[究極/超絶]</option>
        <option value="11">攻略[極]</option>
        <option value="50">攻略[ハクア]</option>
        <option value="91">攻略[火ノエル]</option>
        <option value="12">攻略[神殿]</option>
      </select>
    <br>
    <hr>
    </div>


    <!--
      その他情報
    -->
    <div class="optionSection">
      <h3>オプション</h3>
      <div class="ui-field-contain">
        <fieldset data-role="controlgroup">
          <input type="checkbox" id="autoSelect" name="autoSelect"  data-mini="true">
          <label for="autoSelect">自動選択</label>
          <input type="checkbox" id="autoRefresh" name="autoRefresh"  data-mini="true">
          <label for="autoRefresh">自動更新</label>
        </fieldset>
      </div>
    <br>
    <hr>
    </div>

    <!--
      フィルタ情報
    -->
    <div class="filterSection">
      <h3>フィルタ設定</h3>
      <table>
          <colgroup>
            <col style="width: 7em">
            <col style="width: 21em">
          </colgroup>
        <tr>
          <th align="right">クエスト：</th>
          <td><input type="text" id="filterQuestTitle" data-clear-btn="true"></td>
        </tr>
        <tr>
          <th align="right">メッセージ：</th>
          <td><input type="text" id="filterQuestMsg" data-clear-btn="true"></td>
        </tr>
      </table>
    </div>



  </article>

  <!--
    フッタ
  -->
  <footer data-role="footer" data-theme="b">
    <hr>
    <p><i>Copyright &copy; 2015 壁Corp. All Rights Reserved.</i></p>
  </footer>

</div>

</form>
</body>
</html>