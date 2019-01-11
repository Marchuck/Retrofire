//
//  MainThread.swift
//  KotlinIOS
//
//  Created by Łukasz Marczak on 11.01.2019.
//  Copyright © 2019 Evgeny Petrenko. All rights reserved.
//

import Foundation
import SharedCode

class IosMain : NSObject, Main{
    func post(task: @escaping () -> KotlinUnit) {
        DispatchQueue.main.async {
            task()
        }
    }
}
