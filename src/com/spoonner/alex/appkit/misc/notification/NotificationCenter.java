/*

[The "BSD licence"]
Copyright (c) 2005 Jean Bovet
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
3. The name of the author may not be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package com.spoonner.alex.appkit.misc.notification;

import java.util.*;

public class NotificationCenter {

    private static NotificationCenter defaultCenter = new NotificationCenter();

    private Map observers = new HashMap();

    public static NotificationCenter defaultCenter() {
        return defaultCenter;
    }

    public void addObserver(NotificationObserver observer, String notifName) {
        List obs = (List)observers.get(notifName);
        if(obs == null) {
            obs = new ArrayList();
            observers.put(notifName, obs);
        }
        obs.add(observer);
    }

    public void removeObserver(NotificationObserver observer) {
        for (Iterator iterator = observers.keySet().iterator(); iterator.hasNext();) {
            String notifName = (String) iterator.next();
            List obs = (List)observers.get(notifName);
            for(int i=obs.size()-1; i>=0; i--) {
                NotificationObserver candidate = (NotificationObserver) obs.get(i);
                if(candidate.equals(observer)) {
                    obs.remove(i);
                }
            }
        }
    }

    public void postNotification(Object source, String notifName) {
        List obs = (List)observers.get(notifName);
        if(obs == null)
            return;
        
        for (Iterator iterator = obs.iterator(); iterator.hasNext();) {
            NotificationObserver observer = (NotificationObserver) iterator.next();
            observer.notificationFire(source, notifName);
        }
    }
}
