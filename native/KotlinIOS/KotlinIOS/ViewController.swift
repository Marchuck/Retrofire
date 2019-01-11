//
//  ViewController.swift
//  KotlinIOS
//
//  Created by Evgeny Petrenko on 26.09.18.
//  Copyright © 2018 Evgeny Petrenko. All rights reserved.
//

import UIKit
import SharedCode

class ViewController: UIViewController {
    
    var cancellable: Cancellable?
    
    let endpoint = "######/authentication"
    
    let alamofireExecution = AlamofireExecution()
    
    let main: Main = IosMain()
   
    @IBOutlet weak var titleLabel: UILabel!
    
    @IBOutlet weak var emailInput: UITextField!
    
    @IBOutlet weak var passwordInput: UITextField!
   
    @IBOutlet weak var errorLabel: UILabel!
    
    @IBOutlet weak var progressView: UIActivityIndicatorView!
    
    @IBAction func onLoginClicked(_ sender: Any) {
        let email = emailInput.text ?? ""
        let password = passwordInput.text ?? ""
        
        progressView.startAnimating()
        
        cancellable =  LoginApi(endpoint: endpoint,
                 execution: alamofireExecution,
                 main: main)
            .login(email: email, password: password, user: { user in
                
                self.errorLabel.text = ""
                
                self.progressView.stopAnimating()

                if user == nil {
                    self.errorLabel.textColor = UIColor.red
                    self.errorLabel.text = "User not found"
                }else{
                    self.errorLabel.textColor = UIColor.green
                    let data = user!.json.convertToDictionary()?["data"] as? [String : AnyObject] ?? [ : ]
                    if let latitude = data["latitude"] as? Double,
                        let longitude = data["longitude"] as? Double{
                        self.errorLabel.text = "\(latitude), \(longitude)"
                    }else{
                        self.errorLabel.textColor = UIColor.red
                        self.errorLabel.text = "Invalid loginn or password"

                    }
                    
                }
                return KotlinUnit()
            })
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        titleLabel.text = CommonKt.createApplicationScreenMessage()
        progressView.hidesWhenStopped = true
    }
}


extension String{
    func convertToDictionary(text: String) -> [String: AnyObject]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: AnyObject]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
}
