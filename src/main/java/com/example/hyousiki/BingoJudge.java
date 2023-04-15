package com.example.hyousiki;

public class BingoJudge {

	Globals globals;
	//private int nCheckBingoCountResult;

	public BingoJudge(Globals globals) {
		this.globals = globals;
	}

    Bingo3 bingo3 = new Bingo3();
    Bingo4 bingo4 = new Bingo4();
	///////ビンゴ判定用↓///////////////////////////////////////////////////////////////////////////////////////
	//ビンゴしているか確認するメソッド(上位)
	//  引数(In)
	//  引数(Out)
	//  return        nCheckBingoCountResult        NoBingo=0 Bingo=1 error=-1
	public int check_Bingo()
	{
		int nCheckBingoCountResult = 0;        // error=-1 Count=0~
		int check = 0;            //判定B
		int nTotalBingoCount = 0;
		//正常判断
		if ( globals.g_nSizeBingo < 3 && globals.g_nSizeBingo > 4)
		{
			nCheckBingoCountResult = -1;		//エラー
			//終了処理（メソッドを抜ける）※※
		}
		else
		{
			for (int nIndex = 0; nIndex < 4 && -1 != nCheckBingoCountResult; nIndex++)
			{
				int nBingoCount = 0;
				switch (nIndex)
				{
					case 0:
						// 横メソッド
						nBingoCount = bingoCheckAndCount(globals.g_nSizeBingo,0);

						break;
					case 1:
						// 縦メソッド
						nBingoCount = bingoCheckAndCount(globals.g_nSizeBingo,1);
						break;
					case 2:
						// 右下がりメソッド
						nBingoCount = bingoCheckAndCount(globals.g_nSizeBingo,2);
						break;
					case 3:
						// 右上がりメソッド
						nBingoCount = bingoCheckAndCount(globals.g_nSizeBingo,3);
						break;
					default:
						check = -1;		//error
						break;
				}

				//判定から返された値が-１なら異常とする
				if (nBingoCount == -1)
				{
					nCheckBingoCountResult = -1;
				}
				else
				{
					// ビンゴ数を加算
					nTotalBingoCount = nTotalBingoCount + nBingoCount;
				}
			}

			// Bingo数の保存
            globals.g_nBingoCount = nTotalBingoCount;

			//判定から返された値が-１なら異常とする
			if (check == -1)
			{
				nCheckBingoCountResult = -1;
			}
			else
			{
				// 前回のBingo数と比較
				if(nTotalBingoCount > globals.g_nBingoLog)
				{
					nCheckBingoCountResult = nTotalBingoCount - globals.g_nBingoLog;
				}
				else
				{
					nCheckBingoCountResult = 0;
				}
			}


			//デバッグ用
//        if(nCheckBingoResult==1)
//        {
//            nCheckBingoResult = nCheckBingoResult;
//        }

		}
		return nCheckBingoCountResult;
	}

    //	Bingo判定用関数(行・列・斜め)
    //  引数(In)      int nSizeBingo    ビンゴのサイズ
    //  引数(Out)
    //  return        nBingoCount        nBingoCount=Bingoの数
    private int bingoCheckAndCount(int nSizeBingo, int nCheckType)
    {
        int nBingoCount = 0;		// Bingoの数
		int nOpenCount = 0;         // 開いている数のカウント

        //リストは正常か
        if (globals.status.length != nSizeBingo * nSizeBingo)
        {
            // リストが異常
            nBingoCount = -1;		//error
        }
        else
        {
            // リスト正常

            // ローカル変数宣言部
            int nCheckStates = 0;       // Normal=0 Error=-1
            int nCheckLineVolume = 0;	// チェックするLineの数
            int nGetResult = 0;         // statesリストからの返り値用 (Open=1 Close=0)

            // 正常
            // チェック処理

            // チェックするLine数をセット
            switch (nCheckType)
            {
                case 0:		// 横
                    nCheckLineVolume = nSizeBingo;
                    break;

                case 1:		// 縦
                    nCheckLineVolume = nSizeBingo;
                    break;

                case 2:		// 右下がり
                    nCheckLineVolume = 1;
                    break;

                case 3:		// 右上がり
                    nCheckLineVolume = 1;
                    break;

                default:	// その他
                    nOpenCount = -1;
                    break;
            }

            // 行数分のループ
            for (int nLine = 0; nLine < nCheckLineVolume && nCheckStates != -1; nLine++)		// nLine=横に進む数
            {
                // 先頭行の位置取得
                // nOpenCountのリセット
                int nCheckIndex = -1;
                switch (nCheckType)
                {
                    case 0:		// 横
                        nCheckIndex = nLine * nSizeBingo;
                        nOpenCount = 0;
                        break;

                    case 1:		// 縦
                        nCheckIndex = nLine;
                        nOpenCount = 0;
                        break;

                    case 2:		// 右下がり
                        nCheckIndex = 0;
                        nOpenCount = 0;
                        break;

                    case 3:		// 右上がり
                        nCheckIndex = nSizeBingo - 1;
                        nOpenCount = 0;
                        break;

                    default:		// その他
                        // nOpenCountを異常値に変更
                        nOpenCount = -1;
                        break;
                }

                //行のマス分のループ
                for (int nLoop = 0; nLoop < nSizeBingo && nOpenCount == nLoop; nLoop++)		// 行変更
                {
                    //判定処理メソッド
                    nGetResult = GetStatusData(nCheckIndex);

                    //判定されていたらOpen数を増やす
                    if (nGetResult == 1)
                    {
                        nOpenCount = nOpenCount + 1;
                    }

                    // Indexを次のチェック位置へ移動
                    switch (nCheckType)
                    {
                        case 0:		// 横
                            nCheckIndex = nCheckIndex + 1;
                            break;

                        case 1:		// 縦
                            nCheckIndex = nCheckIndex + nSizeBingo;
                            break;

                        case 2:		// 右下がり
                            nCheckIndex = nCheckIndex + nSizeBingo + 1;
                            break;

                        case 3:		// 右上がり
                            nCheckIndex = nCheckIndex + nSizeBingo - 1;
                            break;

                        default:		// その他
                            // nOpenCountを異常値に変更
                            nOpenCount = -1;
                            break;
                    }
                }

                // Indexを次のチェック位置へ移動
                if (nOpenCount == nSizeBingo)
                {
                    // Bingo
                    nBingoCount = nBingoCount + 1;
                }
                else if (nOpenCount < nSizeBingo && 0 <= nOpenCount)
                {
                    // NoBingo
                }
                else
                {
                    // エラー時処理
                    nCheckStates = -1;
                    nBingoCount = -1;
                }

            }
        }
        return nBingoCount;
    }


    //statesリストの中身を返すメソッド
	//  引数(In)      int nCheckIndex    取得するデータのIndex
	//  引数(Out)
	//  return        nreturn        Open=1 Close=0
	private int GetStatusData(int nCheckIndex) {
		int nReturn = globals.status[nCheckIndex];
		return nReturn;
	}



}
