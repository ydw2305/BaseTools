/*
 * Copyright 2017 Yang DeWang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ydw.www.toolslib.widget.draglist.listener;

import android.view.View;

/**
 * @author 杨德望
 * @date 2018/1/9
 * 描述: 删除item监听
 */
public interface SwipeItemLongClickListener {

    void onItemLongClick(View itemView, int position);

}
