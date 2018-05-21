/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function countDown(time){
    min=Math.floor(time/60);
    if(min<10)
        rmin='0'+min;
    else
        rmin=min;
    rsec=time-(min*60);
    if(rsec<10)
        rsec='0'+rsec;
    return rmin+':'+rsec;
}
function informer(info){               
                $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    '+info+'</div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    </div></div>');
                $('#myModal').modal('show');
            }
function dialPlanRuleOrder(rule){
    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    Deseja realmente alterar a prioridade de execução desta regra?</div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="Controller?modifyOrder='+rule+'" class="btn btn-info">Sim</a>\n\
                    </div></div>');
    $('#myModal').modal('show');
}
function listFeatures(){
    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">Lista de Facilidades</h3> </div>\n\
                    <div class="modal-body">\n\
                    <b><table>\n\
                    <tr><td>Transferência</td><td>*2</td><tr>\n\
                    <tr><td>Captura Chamada</td><td>*8</td><tr>\n\
                    <tr><td>Estaciona Chamada</td><td>*70</td><tr>\n\
                    <tr><td>Recupera Chamada</td><td>*701 - *720</td><tr>\n\
                    <tr><td>Não Perturbe (ativa)</td><td>*21</td></tr>\n\
                    <tr><td>Não Perturbe (desativa)</td><td>*22</td><tr>\n\
                    <tr><td>Cadeado (ativa)</td><td>*23</td><tr>\n\
                    <tr><td>Cadeado (desativa)</td><td>*24</td><tr>\n\
                    <tr><td>Desvio (ativa)</td><td>*25</td><tr>\n\
                    <tr><td>Desvio (desativa)</td><td>*26</td><tr>\n\
                    <tr><td>Rechamada (ocupado)</td><td>5</td><tr>\n\
                    <tr><td>Salas de Conferência </td><td>*901 - *909</td><tr>\n\
                    <tr><td>Consulta Voicemail </td><td>*30</td><tr>\n\
                    </table></b>\n\
                    </div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Fechar</button>\n\
                    </div></div>');
    $('#myModal').modal('show');
}
function newCost(){
    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">Criação de novo Custo</h3> </div>\n\
                    <div class="modal-body">\n\
                    <form id="frm" action="CreateCost" method="POST">\n\
                    <p class="text-info">Tipo do custo</p>\n\
                    <select name="type">\n\
                        <option>Entradas</option>\n\
                        <option>Local</option>\n\
                        <option>DDD</option>\n\
                        <option>VC1</option>\n\
                        <option>VC2</option>\n\
                        <option>VC3</option>\n\
                        <option>DDI</option>\n\
                        <option>Funcionalidades</option>\n\
                        <option>Internas</option>\n\
                    </select>\n\
                    <p class="text-info">Descrição do custo</p>\n\
                    <input id="desc" type="text" name="cost" /></form></div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="#" onclick="goFrm();" class="btn btn-info">Criar</a>\n\
                    </div></div>\n\
                        <script>\n\
                            function goFrm(){$("#frm").submit();}\n\
                         </script>');
    $('#myModal').modal('show');
}
function deleteCall(uniqueid){
    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    Deseja realmente apagar o registro desta ligação?</div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="#" onclick="delCall(\''+uniqueid+'\');" class="btn btn-info">Apagar</a>\n\
                    </div></div>');
    $('#myModal').modal('show');
}
function deleteConference(id,rec){
    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    Deseja realmente apagar o registro desta conferência?</div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="#" onclick="delConf('+id+',\''+rec+'\');" class="btn btn-info">Apagar</a>\n\
                    </div></div>');
    $('#myModal').modal('show');
}
function deleteCompany(name,id){
    if(id!==1){
        $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    Deseja realmente apagar a empresa '+name+'?</div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="DeleteCompany?id='+id+'" class="btn btn-info">Apagar</a>\n\
                    </div></div>');
        $('#myModal').modal('show');
    }
}
function uploadMoh(){
    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">Carregue o novo audio.wav</h3></div>\n\
                    <div class="modal-body">\n\
                    <p class="muted">Max: 6Mb</p>\n\
                    <form method="POST" id="formMoh" action="Uploads" enctype="multipart/form-data">\n\
                        <input type="hidden" name="MAX_FILE_SIZE" value="6000000" />\n\
                        <input type="file" name="arqMoh" id="arq">\n\
                        <input type="button" value="Carregar" onClick="load();">\n\
                        <div id="load"></div>\n\
                    </form>\n\
                    <script>\n\
                            function load(){\n\
                                if($(\'#arq\').val()===\'\')\n\
                                    alert(\'Nenhum arquivo selecionado!\');\n\
                                else{\n\
                                    ext=$(\'#arq\').val().substring($(\'#arq\').val().indexOf(\'.\'));\n\
                                    //alert(ext);\n\
                                    if(ext===\'.wav\'){\n\
                                        $(\'#load\').html(\'<img src="css/bootstrap/img/loadBar.gif"/>\');\n\
                                        setTimeout("submit();",1500);\n\
                                    }\n\
                                    else\n\
                                        alert(\'O Sistema aceita somente audios.wav\');\n\
                                }\n\
                            }\n\
                            function submit(){\n\
                                $(\'#formMoh\').submit();\n\
                            }\n\
                        </script>\n\
                    </div>\n\
                    <div class="modal-footer">\n\
                    </div></div>');
    $('#myModal').modal('show');
}
function uploadPdf(){
    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">Carregue um arquivo PDF</h3></div>\n\
                    <div class="modal-body">\n\
                    <p class="muted">Max: 6Mb</p>\n\
                    <form method="POST" id="formPdf" action="UploadPdf" enctype="multipart/form-data">\n\
                        <input type="hidden" name="MAX_FILE_SIZE" value="6000000" />\n\
                        <input type="file" name="arqPdf" id="arq">\n\
                        <input type="button" value="Carregar" onClick="load();">\n\
                        <div id="load"></div>\n\
                    </form>\n\
                    <script>\n\
                            function load(){\n\
                                if($(\'#arq\').val()===\'\')\n\
                                    alert(\'Nenhum arquivo selecionado!\');\n\
                                else{\n\
                                    ext=$(\'#arq\').val().substring($(\'#arq\').val().indexOf(\'.\'));\n\
                                    //alert(ext);\n\
                                    if(ext===\'.pdf\'){\n\
                                        $(\'#load\').html(\'<img src="css/bootstrap/img/loadBar.gif"/>\');\n\
                                        setTimeout("submit();",1500);\n\
                                    }\n\
                                    else\n\
                                        alert(\'O Sistema aceita somente arquivos.pdf\');\n\
                                }\n\
                            }\n\
                            function submit(){\n\
                                $(\'#formPdf\').submit();\n\
                            }\n\
                        </script>\n\
                    </div>\n\
                    <div class="modal-footer">\n\
                    </div></div>');
    $('#myModal').modal('show');
}
function deleteCos(name,id){
    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    Deseja realmente apagar a Classe de serviço '+name+'?</div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="DeleteCos?idcos='+id+'" class="btn btn-info">Apagar</a>\n\
                    </div></div>');
    $('#myModal').modal('show');
}
function deleteAlias(id,name){
    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    Deseja realmente apagar o Alias '+name+'?</div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="DeleteAlias?id='+id+'" class="btn btn-info">Apagar</a>\n\
                    </div></div>');
    $('#myModal').modal('show');
}
function deleteBkp(id){
    $('#alert').html('<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\n\
                    <div class="modal-header">\n\
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>\n\
                    <h3 id="myModalLabel">JPBX Informa</h3> </div>\n\
                    <div class="modal-body">\n\
                    Deseja realmente apagar este Backup?</div>\n\
                    <div class="modal-footer">\n\
                    <button class="btn disabled" data-dismiss="modal" aria-hidden="true">Cancelar</button>\n\
                    <a href="DeleteBackup?id='+id+'" class="btn btn-info">Apagar</a>\n\
                    </div></div>');
    $('#myModal').modal('show');
}
function validEmail(){
                //var obj = $('#email');
                var txt = $('#email').val();
                if ((txt.length != 0) && ((txt.indexOf("@") < 1) || (txt.indexOf('.') < 7))){
                    $('#email').css('background-color','salmon'); 
                }
                else
                    $('#email').css('background-color','lightgreen');
            }
function hashMD5(string) {
 
	function RotateLeft(lValue, iShiftBits) {
		return (lValue<<iShiftBits) | (lValue>>>(32-iShiftBits));
	}
 
	function AddUnsigned(lX,lY) {
		var lX4,lY4,lX8,lY8,lResult;
		lX8 = (lX & 0x80000000);
		lY8 = (lY & 0x80000000);
		lX4 = (lX & 0x40000000);
		lY4 = (lY & 0x40000000);
		lResult = (lX & 0x3FFFFFFF)+(lY & 0x3FFFFFFF);
		if (lX4 & lY4) {
			return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
		}
		if (lX4 | lY4) {
			if (lResult & 0x40000000) {
				return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
			} else {
				return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
			}
		} else {
			return (lResult ^ lX8 ^ lY8);
		}
 	}
 
 	function F(x,y,z) { return (x & y) | ((~x) & z); }
 	function G(x,y,z) { return (x & z) | (y & (~z)); }
 	function H(x,y,z) { return (x ^ y ^ z); }
	function I(x,y,z) { return (y ^ (x | (~z))); }
 
	function FF(a,b,c,d,x,s,ac) {
		a = AddUnsigned(a, AddUnsigned(AddUnsigned(F(b, c, d), x), ac));
		return AddUnsigned(RotateLeft(a, s), b);
	};
 
	function GG(a,b,c,d,x,s,ac) {
		a = AddUnsigned(a, AddUnsigned(AddUnsigned(G(b, c, d), x), ac));
		return AddUnsigned(RotateLeft(a, s), b);
	};
 
	function HH(a,b,c,d,x,s,ac) {
		a = AddUnsigned(a, AddUnsigned(AddUnsigned(H(b, c, d), x), ac));
		return AddUnsigned(RotateLeft(a, s), b);
	};
 
	function II(a,b,c,d,x,s,ac) {
		a = AddUnsigned(a, AddUnsigned(AddUnsigned(I(b, c, d), x), ac));
		return AddUnsigned(RotateLeft(a, s), b);
	};
 
	function ConvertToWordArray(string) {
		var lWordCount;
		var lMessageLength = string.length;
		var lNumberOfWords_temp1=lMessageLength + 8;
		var lNumberOfWords_temp2=(lNumberOfWords_temp1-(lNumberOfWords_temp1 % 64))/64;
		var lNumberOfWords = (lNumberOfWords_temp2+1)*16;
		var lWordArray=Array(lNumberOfWords-1);
		var lBytePosition = 0;
		var lByteCount = 0;
		while ( lByteCount < lMessageLength ) {
			lWordCount = (lByteCount-(lByteCount % 4))/4;
			lBytePosition = (lByteCount % 4)*8;
			lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount)<<lBytePosition));
			lByteCount++;
		}
		lWordCount = (lByteCount-(lByteCount % 4))/4;
		lBytePosition = (lByteCount % 4)*8;
		lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80<<lBytePosition);
		lWordArray[lNumberOfWords-2] = lMessageLength<<3;
		lWordArray[lNumberOfWords-1] = lMessageLength>>>29;
		return lWordArray;
	};
 
	function WordToHex(lValue) {
		var WordToHexValue="",WordToHexValue_temp="",lByte,lCount;
		for (lCount = 0;lCount<=3;lCount++) {
			lByte = (lValue>>>(lCount*8)) & 255;
			WordToHexValue_temp = "0" + lByte.toString(16);
			WordToHexValue = WordToHexValue + WordToHexValue_temp.substr(WordToHexValue_temp.length-2,2);
		}
		return WordToHexValue;
	};
 
	function Utf8Encode(string) {
		string = string.replace(/\r\n/g,"\n");
		var utftext = "";
 
		for (var n = 0; n < string.length; n++) {
 
			var c = string.charCodeAt(n);
 
			if (c < 128) {
				utftext += String.fromCharCode(c);
			}
			else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			}
			else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}
 
		}
 
		return utftext;
	};
 
	var x=Array();
	var k,AA,BB,CC,DD,a,b,c,d;
	var S11=7, S12=12, S13=17, S14=22;
	var S21=5, S22=9 , S23=14, S24=20;
	var S31=4, S32=11, S33=16, S34=23;
	var S41=6, S42=10, S43=15, S44=21;
 
	string = Utf8Encode(string);
 
	x = ConvertToWordArray(string);
 
	a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476;
 
	for (k=0;k<x.length;k+=16) {
		AA=a; BB=b; CC=c; DD=d;
		a=FF(a,b,c,d,x[k+0], S11,0xD76AA478);
		d=FF(d,a,b,c,x[k+1], S12,0xE8C7B756);
		c=FF(c,d,a,b,x[k+2], S13,0x242070DB);
		b=FF(b,c,d,a,x[k+3], S14,0xC1BDCEEE);
		a=FF(a,b,c,d,x[k+4], S11,0xF57C0FAF);
		d=FF(d,a,b,c,x[k+5], S12,0x4787C62A);
		c=FF(c,d,a,b,x[k+6], S13,0xA8304613);
		b=FF(b,c,d,a,x[k+7], S14,0xFD469501);
		a=FF(a,b,c,d,x[k+8], S11,0x698098D8);
		d=FF(d,a,b,c,x[k+9], S12,0x8B44F7AF);
		c=FF(c,d,a,b,x[k+10],S13,0xFFFF5BB1);
		b=FF(b,c,d,a,x[k+11],S14,0x895CD7BE);
		a=FF(a,b,c,d,x[k+12],S11,0x6B901122);
		d=FF(d,a,b,c,x[k+13],S12,0xFD987193);
		c=FF(c,d,a,b,x[k+14],S13,0xA679438E);
		b=FF(b,c,d,a,x[k+15],S14,0x49B40821);
		a=GG(a,b,c,d,x[k+1], S21,0xF61E2562);
		d=GG(d,a,b,c,x[k+6], S22,0xC040B340);
		c=GG(c,d,a,b,x[k+11],S23,0x265E5A51);
		b=GG(b,c,d,a,x[k+0], S24,0xE9B6C7AA);
		a=GG(a,b,c,d,x[k+5], S21,0xD62F105D);
		d=GG(d,a,b,c,x[k+10],S22,0x2441453);
		c=GG(c,d,a,b,x[k+15],S23,0xD8A1E681);
		b=GG(b,c,d,a,x[k+4], S24,0xE7D3FBC8);
		a=GG(a,b,c,d,x[k+9], S21,0x21E1CDE6);
		d=GG(d,a,b,c,x[k+14],S22,0xC33707D6);
		c=GG(c,d,a,b,x[k+3], S23,0xF4D50D87);
		b=GG(b,c,d,a,x[k+8], S24,0x455A14ED);
		a=GG(a,b,c,d,x[k+13],S21,0xA9E3E905);
		d=GG(d,a,b,c,x[k+2], S22,0xFCEFA3F8);
		c=GG(c,d,a,b,x[k+7], S23,0x676F02D9);
		b=GG(b,c,d,a,x[k+12],S24,0x8D2A4C8A);
		a=HH(a,b,c,d,x[k+5], S31,0xFFFA3942);
		d=HH(d,a,b,c,x[k+8], S32,0x8771F681);
		c=HH(c,d,a,b,x[k+11],S33,0x6D9D6122);
		b=HH(b,c,d,a,x[k+14],S34,0xFDE5380C);
		a=HH(a,b,c,d,x[k+1], S31,0xA4BEEA44);
		d=HH(d,a,b,c,x[k+4], S32,0x4BDECFA9);
		c=HH(c,d,a,b,x[k+7], S33,0xF6BB4B60);
		b=HH(b,c,d,a,x[k+10],S34,0xBEBFBC70);
		a=HH(a,b,c,d,x[k+13],S31,0x289B7EC6);
		d=HH(d,a,b,c,x[k+0], S32,0xEAA127FA);
		c=HH(c,d,a,b,x[k+3], S33,0xD4EF3085);
		b=HH(b,c,d,a,x[k+6], S34,0x4881D05);
		a=HH(a,b,c,d,x[k+9], S31,0xD9D4D039);
		d=HH(d,a,b,c,x[k+12],S32,0xE6DB99E5);
		c=HH(c,d,a,b,x[k+15],S33,0x1FA27CF8);
		b=HH(b,c,d,a,x[k+2], S34,0xC4AC5665);
		a=II(a,b,c,d,x[k+0], S41,0xF4292244);
		d=II(d,a,b,c,x[k+7], S42,0x432AFF97);
		c=II(c,d,a,b,x[k+14],S43,0xAB9423A7);
		b=II(b,c,d,a,x[k+5], S44,0xFC93A039);
		a=II(a,b,c,d,x[k+12],S41,0x655B59C3);
		d=II(d,a,b,c,x[k+3], S42,0x8F0CCC92);
		c=II(c,d,a,b,x[k+10],S43,0xFFEFF47D);
		b=II(b,c,d,a,x[k+1], S44,0x85845DD1);
		a=II(a,b,c,d,x[k+8], S41,0x6FA87E4F);
		d=II(d,a,b,c,x[k+15],S42,0xFE2CE6E0);
		c=II(c,d,a,b,x[k+6], S43,0xA3014314);
		b=II(b,c,d,a,x[k+13],S44,0x4E0811A1);
		a=II(a,b,c,d,x[k+4], S41,0xF7537E82);
		d=II(d,a,b,c,x[k+11],S42,0xBD3AF235);
		c=II(c,d,a,b,x[k+2], S43,0x2AD7D2BB);
		b=II(b,c,d,a,x[k+9], S44,0xEB86D391);
		a=AddUnsigned(a,AA);
		b=AddUnsigned(b,BB);
		c=AddUnsigned(c,CC);
		d=AddUnsigned(d,DD);
	}
 
	var temp = WordToHex(a)+WordToHex(b)+WordToHex(c)+WordToHex(d);
 
	return temp.toLowerCase();
}