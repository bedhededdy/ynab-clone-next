"use client";

import { useForm } from "react-hook-form";

import Link from "next/link";

import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";

import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardContent, CardTitle, CardFooter } from "@/components/ui/card";
import { Form } from "@/components/ui/form";

import EmailFormControl from "@/components/EmailFormControl";
import PasswordFormControl from "@/components/PasswordFormControl";

const formSchema = z.object({
  email: z.string().email(),
  password: z.string().min(6).max(50),
  confirmPassword: z.string().min(6).max(50),
}).refine(data => data.password === data.confirmPassword, {
  message: "Passwords do not match",
  path: ["confirmPassword"],
});

type FormValues = z.infer<typeof formSchema>;

const Page: React.FC = () => {
  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: "",
      confirmPassword: "",
    }
  });

  const onSubmit = (values: FormValues) => {
    console.log(values);
  }

  return (
    <main className="min-w-dvw min-h-dvh flex justify-center items-center">
      <Card>
        <CardHeader>
          <CardTitle>Login</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-4 w-full items-center">
              <EmailFormControl control={form.control} name="email" />
              <PasswordFormControl control={form.control} name="password" dispName="Password" />
              <PasswordFormControl control={form.control} name="confirmPassword" dispName="Confirm Password" />
              <Button type="submit" className="w-1/3">Login</Button>
            </form>
          </Form>
        </CardContent>
        <CardFooter>
          <div className="flex flex-col w-full items-end">
            <Link href="/login">Login</Link>
          </div>
        </CardFooter>
      </Card>
    </main>
  )
}

export default Page;
