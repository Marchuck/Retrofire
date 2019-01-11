//
//  AlamofireExecution.swift
//  KotlinIOS
//
//  Created by Łukasz Marczak on 11.01.2019.
//  Copyright © 2019 Evgeny Petrenko. All rights reserved.
//

import Foundation
import SharedCode
import Alamofire

class AlamofireExecution: NSObject, Execution {
    
    func execute(httpVerb: HttpVerb,
                 url: String,
                 requestBody: String,
                 queries: [String : String],
                 onSuccess: @escaping (String) -> KotlinUnit,
                 onFailure: @escaping (KotlinThrowable) -> KotlinUnit) -> Cancellable {
        
        var dataRequest : Alamofire.DataRequest? = nil
        
        if httpVerb == .post {
            
            let parameters = requestBody.convertToDictionary()
            print("parameters \(parameters)")
            dataRequest = Alamofire.request(URL(string: url)!, method: .post,
                                            parameters: parameters, encoding: JSONEncoding.default, headers: nil)
                .responseJSON { response in
                    
                    if let jsonDict = response.result.value as? NSDictionary {
                       
                        do{
                            let jsonData = try JSONSerialization.data(withJSONObject: jsonDict)
                            if let json = String(data: jsonData, encoding: .utf8){
                                onSuccess(json)
                            }
                        }catch{
                            onFailure(KotlinThrowable(message: "failed to execute POST \(error)"))
                        }
                        
                    }else{
                        onFailure(KotlinThrowable(message: "failed to execute POST \(url)"))
                    }
            }
        }else if httpVerb == .get{
            
            var queryBuilder = "?"
            for key in queries.keys{
                let value = queries[key] ?? ""
                queryBuilder += "\(key)=\(value)&"
            }
            let query = queryBuilder.dropLast()
            
            let fullUrl = url + query
            
            dataRequest = Alamofire.request(URL(string: fullUrl)!, method: .get,
                                            parameters: nil, encoding: JSONEncoding.default, headers: nil)
                .responseJSON { response in
                    if let jsonData = response.result.value as? String {
                        onSuccess(jsonData)
                    }else{
                        onFailure(KotlinThrowable(message: "failed to execute GET \(fullUrl)"))
                    }
            }
        }else if httpVerb == .patch{
            
        }else if httpVerb == .put{
            
        }else if httpVerb == .delete{
            
        }
        
        return CancellableDataRequest(dataRequest: dataRequest)
    }
}

class CancellableDataRequest : NSObject, Cancellable{
    
    let dataRequest: Alamofire.DataRequest?
    
    init(dataRequest: Alamofire.DataRequest?) {
        self.dataRequest = dataRequest
    }
    func cancel() {
        dataRequest?.cancel()
    }
}


extension String {
    func convertToDictionary() -> [String: Any]? {
        if let data = self.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
}
