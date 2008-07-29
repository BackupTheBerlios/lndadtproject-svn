/***
 * * PROJECT
 * *    Logical Neighborhoods - J2ME Run-Time Support 
 * * VERSION
 * *    $LastChangedRevision: 118 $
 * * DATE
 * *    $LastChangedDate: 2008-05-28 19:35:12 +0200 (Wed, 28 May 2008) $
 * * LAST_CHANGE_BY
 * *    $LastChangedBy: lmottola $
 * *
 * *    $Id: SystemClock.java 118 2008-05-28 17:35:12Z lmottola $
 * *
 * *   Logical Neighborhoods - 
 * *                   Programming Wireless Sensor Networks
 * *                   (One Slice at a Time)
 * *
 * *   This program is free software; you can redistribute it and/or
 * *   modify it under the terms of the GNU Lesser General Public License
 * *   as published by the Free Software Foundation; either version 2.1
 * *   of the License, or (at your option) any later version.
 * *
 * *   This program is distributed in the hope that it will be useful,
 * *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * *   GNU General Public License for more details.
 * *
 * *   You should have received a copy of the GNU Lesser General Public 
 * *   License along with this program; if not, you may find a copy at 
 * *   the FSF web site at 'www.gnu.org' or 'www.fsf.org', or you may 
 * *   write to the Free Software Foundation, Inc., 59 Temple 
 * *   Place - Suite 330, Boston, MA  02111-1307, USA
 ***/

package polimi.ln.runtime.platformDependent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import polimi.ln.runtime.Clock;


public class SystemClock implements Clock {

  private static DateFormat m_dateFormat = new SimpleDateFormat("yyyyMMdd");
  private static DateFormat m_timeFormat = new SimpleDateFormat("HHmmss");
  private static Date m_today;

  public String getDate() {
       	m_today = new Date();
       	String nowDate = m_dateFormat.format(m_today);
		return nowDate;
	}

  public String getTime() {
       	m_today = new Date();
       	String nowTime = m_timeFormat.format(m_today);
		return nowTime;
	}

}
