"use client";

import { useForm } from "react-hook-form";

import Link from "next/link";
import { useRouter } from "next/navigation";

import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";

import { Button } from "@/app/_components/ui/button";
import { Card, CardHeader, CardContent, CardTitle, CardFooter } from "@/app/_components/ui/card";
import { Form } from "@/app/_components/ui/form";

import EmailFormControl from "@/app/_components/EmailFormControl";
import PasswordFormControl from "@/app/_components/PasswordFormControl";

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
  const router = useRouter();

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: "",
      confirmPassword: "",
    }
  });

  const { isSubmitting } = form.formState;

  const onSubmit = async (values: FormValues) => {
    const response = await fetch("/api/signup", {
      method: "POST",
      body: JSON.stringify(values),
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json"
      }
    });
    if (response.ok) {
      router.push("/login");
    } else {
      const message = response.status >= 500 ? "Server error" : "User already registered with this email";
      form.setError("email", { message });
    }
  }

  return (
    <main className="min-w-dvw min-h-dvh flex justify-center items-center">
      <Card>
        <CardHeader>
          <CardTitle>Sign Up</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-4 w-full items-center">
              <EmailFormControl control={form.control} name="email" />
              <PasswordFormControl control={form.control} name="password" dispName="Password" />
              <PasswordFormControl control={form.control} name="confirmPassword" dispName="Confirm Password" />
              <Button type="submit" className="w-1/3" disabled={isSubmitting}>Sign Up</Button>
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
